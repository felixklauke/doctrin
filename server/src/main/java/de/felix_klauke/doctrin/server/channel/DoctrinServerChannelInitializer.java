package de.felix_klauke.doctrin.server.channel;

import de.felix_klauke.doctrin.commons.codec.JsonDecoder;
import de.felix_klauke.doctrin.commons.codec.JsonEncoder;
import de.felix_klauke.doctrin.server.connection.DoctrinServerConnectionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ServerChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import javax.inject.Inject;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinServerChannelInitializer extends ChannelInitializer<ServerChannel> {

    /**
     * The server connection factory.
     */
    private DoctrinServerConnectionFactory serverConnectionFactory;

    @Inject
    public DoctrinServerChannelInitializer(DoctrinServerConnectionFactory serverConnectionFactory) {
        this.serverConnectionFactory = serverConnectionFactory;
    }

    @Override
    protected void initChannel(ServerChannel serverChannel) {
        ChannelPipeline channelPipeline = serverChannel.pipeline();
        channelPipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4));
        channelPipeline.addLast(new JsonDecoder());
        channelPipeline.addLast(new LengthFieldPrepender(4));
        channelPipeline.addLast(new JsonEncoder());
        channelPipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
        channelPipeline.addLast((ChannelHandler) serverConnectionFactory.createConnection());
    }
}
