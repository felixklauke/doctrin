package de.felix_klauke.doctrin.server.provider;

import de.felix_klauke.doctrin.core.DoctrinCoreApplicationImpl;
import de.felix_klauke.doctrin.server.DoctrinServerApplicationImpl;
import de.felix_klauke.doctrin.server.channel.DoctrinServerChannelInitializer;
import de.felix_klauke.doctrin.server.connection.DoctrinNettyServerConnection;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Provider;

import static org.junit.Assert.assertNotNull;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class ServerBootstrapProviderTest {

    private Provider<ServerBootstrap> serverBootstrapProvider;

    @Before
    public void setUp() {
        serverBootstrapProvider = new ServerBootstrapProvider(NioServerSocketChannel.class, new DoctrinServerChannelInitializer(DoctrinNettyServerConnection::new, new DoctrinServerApplicationImpl(new DoctrinCoreApplicationImpl(subscriptionManager))));
    }

    @Test
    public void get() {
        ServerBootstrap serverBootstrap = serverBootstrapProvider.get();

        assertNotNull(serverBootstrap);
    }
}