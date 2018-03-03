package de.felix_klauke.doctrin.client.net;

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
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.Queue;

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
    private Observable<JSONObject> messages;
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
        messages = doctrinClientConnection.getMessages();
        clientConnection = doctrinClientConnection;

        clientConnection.getConnected().filter(aBoolean -> !aBoolean).subscribe(aBoolean -> connect().retryWhen(throwableObservable -> throwableObservable
                .flatMap(throwable -> {
                            if (throwable instanceof IOException) {
                                System.out.println("Connecting failed: " + throwable.getMessage() + " Retrying...");
                                return Observable.timer(1, TimeUnit.SECONDS);
                            }

                            return Observable.error(throwable);
                        }
                )).subscribe(aBoolean1 -> System.out.println("Successfully reconnected!")));
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
    public void sendMessage(JSONObject jsonObject) {
        if (isConnected()) {
            sendingQueue.offer(jsonObject);
            return;
        }

        clientConnection.sendMessage(jsonObject);
    }

    @Override
    public boolean isConnected() {
        return !workerGroup.isShutdown() && channel.isActive();
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
        while (isConnected() && sendingQueue.isEmpty()) {
            JSONObject jsonObject = sendingQueue.poll();

            sendMessage(jsonObject);
        }
    }
}
