package de.felix_klauke.doctrin.server.channel;

import de.felix_klauke.doctrin.commons.codec.JsonDecoder;
import de.felix_klauke.doctrin.commons.codec.JsonEncoder;
import de.felix_klauke.doctrin.server.DoctrinServerApplication;
import de.felix_klauke.doctrin.server.connection.DoctrinServerConnection;
import de.felix_klauke.doctrin.server.connection.DoctrinServerConnectionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import javax.inject.Inject;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinServerChannelInitializer extends ChannelInitializer<Channel> {

    /**
     * The server connection factory.
     */
    private final DoctrinServerConnectionFactory serverConnectionFactory;

    /**
     * The instance of the server application.
     */
    private final DoctrinServerApplication serverApplication;

    @Inject
    public DoctrinServerChannelInitializer(DoctrinServerConnectionFactory serverConnectionFactory, DoctrinServerApplication serverApplication) {
        this.serverConnectionFactory = serverConnectionFactory;
        this.serverApplication = serverApplication;
    }

    @Override
    protected void initChannel(Channel serverChannel) {
        ChannelPipeline channelPipeline = serverChannel.pipeline();
        channelPipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4));
        channelPipeline.addLast(new JsonDecoder());
        channelPipeline.addLast(new LengthFieldPrepender(4));
        channelPipeline.addLast(new JsonEncoder());
        //channelPipeline.addLast(new LoggingHandler(LogLevel.DEBUG));

        DoctrinServerConnection connection = serverConnectionFactory.createConnection();
        if (!(connection instanceof ChannelHandler)) {
            throw new IllegalStateException("Connection has to be a channel handler if working with netty.");
        }

        channelPipeline.addLast((ChannelHandler) connection);

        serverApplication.handleNewConnection(connection);
    }
}
