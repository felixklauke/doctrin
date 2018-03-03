package de.felix_klauke.doctrin.server.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinNettyServer implements DoctrinServer {

    private final Logger logger = LoggerFactory.getLogger(DoctrinNettyServer.class);

    /**
     * The host the server is listening on.
     */
    private final String serverHost;

    /**
     * The port the server will be bound to.
     */
    private final int serverPort;

    /**
     * The netty boss group.
     */
    private final EventLoopGroup bossGroup;

    /**
     * The netty worker group.
     */
    private final EventLoopGroup workerGroup;

    /**
     * The provider if the server bootstrap. This should be injected using a provider because we maybe won't need
     * the server bootstrap and will provide it on demand.
     */
    private final Provider<ServerBootstrap> serverBootstrapProvider;

    /**
     * The channel of the server.
     */
    private Channel channel;

    @Inject
    public DoctrinNettyServer(@Named("serverHost") String serverHost, @Named("serverPort") int serverPort,
                              @Named("bossGroup") EventLoopGroup bossGroup, @Named("workerGroup") EventLoopGroup workerGroup,
                              Provider<ServerBootstrap> serverBootstrapProvider) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.bossGroup = bossGroup;
        this.workerGroup = workerGroup;
        this.serverBootstrapProvider = serverBootstrapProvider;
    }

    @Override
    public void start() {
        logger.info("Initializing server bootstrap.");

        ServerBootstrap serverBootstrap = serverBootstrapProvider.get();

        try {
            logger.info("Binding server bootstrap on {}:{}.", serverHost, serverPort);

            channel = serverBootstrap
                    .group(bossGroup, workerGroup)
                    .bind(serverHost, serverPort)
                    .sync()
                    .channel();

            logger.info("Bound server bootstrap on {}:{}.", serverHost, serverPort);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isRunning() {
        return channel != null && channel.isActive();
    }

    @Override
    public void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();

        if (channel == null) {
            return;
        }

        channel.close();
    }
}
