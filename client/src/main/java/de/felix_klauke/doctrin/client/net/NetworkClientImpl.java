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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class NetworkClientImpl implements NetworkClient {

    private final Logger logger = LoggerFactory.getLogger(NetworkClientImpl.class);
    private final Queue<JSONObject> sendingQueue = Queues.newConcurrentLinkedQueue();
    private final EventLoopGroup workerGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
    private final String host;
    private final int port;
    private final DoctrinClientChannelInitializer channelInitializer;
    private Channel channel;
    private final PublishSubject<JSONObject> messages = PublishSubject.create();
    private final PublishSubject<Boolean> reconnects = PublishSubject.create();
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
                                    reconnects.onNext(true);

                                    logger.error("Connecting failed: {} - Retrying...", throwable.getMessage());
                                    return Observable.timer(1, TimeUnit.SECONDS);
                                }

                                return Observable.error(throwable);
                            }
                    ))
                    .subscribe(aBoolean1 -> {
                        logger.info("Reconnected successfully.");
                        reconnects.onNext(true);
                    });
        });
    }

    @Override
    public Observable<Boolean> connect() {
        return Observable.create(observableEmitter -> {
            try {
                logger.info("Connecting to {}:{}.", host, port);

                Bootstrap bootstrap = new Bootstrap()
                        .group(workerGroup)
                        .handler(channelInitializer)
                        .channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY, true);

                channel = bootstrap.connect(host, port)
                        .sync().channel();

                logger.info("Connected to {}:{} successfully.", host, port);

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
    public void disconnect() {
        workerGroup.shutdownGracefully();
        messages.onComplete();
        reconnects.onComplete();
    }

    @Override
    public void sendMessage(JSONObject jsonObject) {
        Preconditions.checkNotNull(jsonObject, "Message may not be null.");

        if (!isConnected()) {
            logger.info("It seems like there is no connection to the server. Storing message {} in sending queue.", jsonObject);

            sendingQueue.offer(jsonObject);
            return;
        }

        clientConnection.sendMessage(jsonObject);
    }

    @Override
    public Observable<Boolean> getReconnect() {
        return reconnects;
    }

    @Override
    public Observable<JSONObject> getMessages() {
        return messages;
    }

    /**
     * Process all elements that are left in the sending queue.
     */
    private void processSendingQueue() {
        logger.info("Working on Sending Queue... Sending queue contains {} items.", sendingQueue.size());

        while (!sendingQueue.isEmpty()) {
            JSONObject jsonObject = sendingQueue.poll();

            sendMessage(jsonObject);
        }
    }
}
