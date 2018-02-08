package de.felix_klauke.doctrin.server.provider;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFactory;
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

    private final ServerBootstrap serverBootstrap;

    @Inject
    public ServerBootstrapProvider(ChannelFactory<ServerChannel> serverChannelChannelFactory, ChannelInitializer<ServerChannel> channelInitializer) {
        this.serverBootstrap = new ServerBootstrap()
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(channelInitializer)
                .channelFactory(serverChannelChannelFactory);
    }

    @Override
    public ServerBootstrap get() {
        return serverBootstrap;
    }
}
