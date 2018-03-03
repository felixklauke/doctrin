package de.felix_klauke.doctrin.server.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Named;
import de.felix_klauke.doctrin.core.module.DoctrinCoreModule;
import de.felix_klauke.doctrin.server.channel.DoctrinServerChannelInitializer;
import de.felix_klauke.doctrin.server.config.DoctrinServerConfig;
import de.felix_klauke.doctrin.server.connection.DoctrinNettyServerConnection;
import de.felix_klauke.doctrin.server.connection.DoctrinServerConnection;
import de.felix_klauke.doctrin.server.connection.DoctrinServerConnectionFactory;
import de.felix_klauke.doctrin.server.network.DoctrinNettyServer;
import de.felix_klauke.doctrin.server.network.DoctrinServer;
import de.felix_klauke.doctrin.server.provider.ServerBootstrapProvider;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinNettyServerModule extends AbstractModule {

    private final DoctrinServerConfig doctrinServerConfig;

    DoctrinNettyServerModule(DoctrinServerConfig doctrinServerConfig) {
        this.doctrinServerConfig = doctrinServerConfig;
    }

    @Override
    protected void configure() {
        // ChannelInitializer
        bind(new TypeLiteral<ChannelInitializer<Channel>>() {
        }).to(DoctrinServerChannelInitializer.class).asEagerSingleton();

        // Bootstrap
        bind(ServerBootstrap.class).toProvider(ServerBootstrapProvider.class);

        // Connection
        install(new FactoryModuleBuilder()
                .implement(DoctrinServerConnection.class, DoctrinNettyServerConnection.class)
                .build(DoctrinServerConnectionFactory.class));

        // Server
        bind(DoctrinServer.class).to(DoctrinNettyServer.class).asEagerSingleton();

        // Core
        install(new DoctrinCoreModule());
    }

    @Provides
    @Named("bossGroup")
    private EventLoopGroup providesBossGroup() {
        int bossGroupSize = doctrinServerConfig.getBossGroupSize();
        return Epoll.isAvailable() ? new EpollEventLoopGroup(bossGroupSize) : new NioEventLoopGroup(bossGroupSize);
    }

    @Provides
    @Named("workerGroup")
    private EventLoopGroup providesWorkerGroup() {
        int workerGroupSize = doctrinServerConfig.getWorkerGroupSize();
        return Epoll.isAvailable() ? new EpollEventLoopGroup(workerGroupSize) : new NioEventLoopGroup(workerGroupSize);
    }

    @Provides
    private Class<? extends ServerChannel> providesServerChannelClass() {
        return Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
    }
}
