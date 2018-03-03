package de.felix_klauke.doctrin.server.network;

import de.felix_klauke.doctrin.core.DoctrinCoreApplicationImpl;
import de.felix_klauke.doctrin.core.subscription.SubscriptionManager;
import de.felix_klauke.doctrin.server.DoctrinServerApplicationImpl;
import de.felix_klauke.doctrin.server.channel.DoctrinServerChannelInitializer;
import de.felix_klauke.doctrin.server.connection.DoctrinNettyServerConnection;
import de.felix_klauke.doctrin.server.provider.ServerBootstrapProvider;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinNettyServerTest {

    private DoctrinServer doctrinServer;

    @Before
    public void setUp() {
        doctrinServer = new DoctrinNettyServer("localhost", 9999, new NioEventLoopGroup(1),
                new NioEventLoopGroup(4), new ServerBootstrapProvider(NioServerSocketChannel.class,
                new DoctrinServerChannelInitializer(DoctrinNettyServerConnection::new, new DoctrinServerApplicationImpl(new DoctrinCoreApplicationImpl(Mockito.mock(SubscriptionManager.class)), doctrinServer))));
    }

    @Test
    public void start() {
        doctrinServer.start();

        doctrinServer.stop();
    }

    @After
    public void tearDown() throws Exception {
        Thread.sleep(1000);
    }
}