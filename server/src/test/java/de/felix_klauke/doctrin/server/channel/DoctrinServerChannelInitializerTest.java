package de.felix_klauke.doctrin.server.channel;

import de.felix_klauke.doctrin.server.connection.DoctrinNettyServerConnection;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinServerChannelInitializerTest {

    private DoctrinServerChannelInitializer doctrinServerChannelInitializer;

    @Before
    public void setUp() {
        doctrinServerChannelInitializer = new DoctrinServerChannelInitializer(DoctrinNettyServerConnection::new);
    }

    @Test
    public void initChannel() {
        NioServerSocketChannel channel = new NioServerSocketChannel();
        doctrinServerChannelInitializer.initChannel(channel);
        assertTrue(channel.pipeline().toMap().size() > 0);
    }
}