package de.felix_klauke.doctrin.client.net;

import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import de.felix_klauke.doctrin.client.channel.DoctrinClientChannelInitializer;
import de.felix_klauke.doctrin.client.connection.DoctrinClientConnection;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class NetworkClientImpl implements NetworkClient {

    private final Queue<JSONObject> sendingQueue = Queues.newConcurrentLinkedQueue();
    private final EventLoopGroup workerGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
    private final String host;
    private final int port;
    private final DoctrinClientChannelInitializer channelInitializer;
    private Channel channel;
    private PublishSubject<JSONObject> messages = PublishSubject.create();
    private DoctrinClientConnection clientConnection;

    public NetworkClientImpl(String host, int port) {
        this.host = host;
        this.port = port;

        channelInitializer = new DoctrinClientChannelInitializer();
        Observable<DoctrinClientConnection> clientConnections = channelInitializer.getClientConnections();
        clientConnections.subscribe(this::handleClientConnection);
    }

    /**
     * Handle that the given connection was created.
     *
     * @param doctrinClientConnection The connection.
     */
    private void handleClientConnection(DoctrinClientConnection doctrinClientConnection) {
        clientConnection = doctrinClientConnection;

        clientConnection.getMessages().subscribe(messages);

        clientConnection.getConnected().subscribe(aBoolean -> {
            if (aBoolean) {
                processSendingQueue();
                return;
            }

            connect().retryWhen(throwableObservable -> throwableObservable
                    .flatMap(throwable -> {
                                if (throwable instanceof IOException) {
                                    System.out.println("Connecting failed: " + throwable.getMessage() + " Retrying...");
                                    return Observable.timer(1, TimeUnit.SECONDS);
                                }

                                return Observable.error(throwable);
                            }
                    )).subscribe(aBoolean1 -> System.out.println("Successfully reconnected!"));
        });
    }

    @Override
    public Observable<Boolean> connect() {
        return Observable.create(observableEmitter -> {
            try {
                Bootstrap bootstrap = new Bootstrap()
                        .group(workerGroup)
                        .handler(channelInitializer)
                        .channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY, true);

                channel = bootstrap.connect(host, port)
                        .sync().channel();

                observableEmitter.onNext(true);
                observableEmitter.onComplete();
            } catch (InterruptedException e) {
                observableEmitter.onError(e);
            }
        });
    }

    @Override
    public boolean isConnected() {
        return !workerGroup.isShutdown() && channel != null && channel.isActive();
    }

    @Override
    public void sendMessage(JSONObject jsonObject) {
        Preconditions.checkNotNull(jsonObject, "Message may not be null.");

        if (!isConnected()) {
            System.out.println("It seems like there is no connection to the server. Storing message " + jsonObject + " in sending queue.");

            sendingQueue.offer(jsonObject);
            return;
        }

        clientConnection.sendMessage(jsonObject);
    }

    @Override
    public void disconnect() {
        workerGroup.shutdownGracefully();
    }

    @Override
    public Observable<JSONObject> getMessages() {
        return messages;
    }

    /**
     * Process all elements that are left in the sending queue.
     */
    private void processSendingQueue() {
        System.out.println("Working on Sending Queue...");

        while (!sendingQueue.isEmpty()) {
            JSONObject jsonObject = sendingQueue.poll();

            sendMessage(jsonObject);
        }
    }
}
