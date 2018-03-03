package de.felix_klauke.doctrin.client.net;

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

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class NetworkClientImpl implements NetworkClient {

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
    }

    @Override
    public void connect() {
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(workerGroup)
                    .handler(channelInitializer)
                    .channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true);

            channel = bootstrap.connect(host, port)
                    .sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    @Override
    public void sendMessage(JSONObject jsonObject) {
        clientConnection.sendMessage(jsonObject);
    }
}
