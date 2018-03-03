package de.felix_klauke.doctrin.server.provider;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ServerChannel;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * The provider that delivers the server bootstrap.
 *
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class ServerBootstrapProvider implements Provider<ServerBootstrap> {

    private final Class<? extends ServerChannel> serverChannelClazz;
    private final ChannelInitializer<Channel> channelInitializer;

    @Inject
    public ServerBootstrapProvider(Class<? extends ServerChannel> serverChannelClazz, ChannelInitializer<Channel> channelInitializer) {
        this.serverChannelClazz = serverChannelClazz;
        this.channelInitializer = channelInitializer;
    }

    @Override
    public ServerBootstrap get() {
        return new ServerBootstrap()
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(channelInitializer)
                .channel(serverChannelClazz);
    }
}
