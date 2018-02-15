package de.felix_klauke.doctrin.server.channel;

import de.felix_klauke.doctrin.commons.message.DoctrinMessageWrapper;
import de.felix_klauke.doctrin.core.DoctrinCoreApplicationImpl;
import de.felix_klauke.doctrin.server.DoctrinServerApplicationImpl;
import de.felix_klauke.doctrin.server.connection.DoctrinNettyServerConnection;
import de.felix_klauke.doctrin.server.connection.DoctrinServerConnection;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.reactivex.Observable;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinServerChannelInitializerTest {

    private DoctrinServerChannelInitializer doctrinServerChannelInitializer;

    @Before
    public void setUp() {
        doctrinServerChannelInitializer = new DoctrinServerChannelInitializer(DoctrinNettyServerConnection::new, new DoctrinServerApplicationImpl(new DoctrinCoreApplicationImpl(subscriptionManager)));
    }

    @Test
    public void initChannel() {
        NioServerSocketChannel channel = new NioServerSocketChannel();
        doctrinServerChannelInitializer.initChannel(channel);
        assertTrue(channel.pipeline().toMap().size() > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void initChannel1() {
        doctrinServerChannelInitializer = new DoctrinServerChannelInitializer(() -> new DoctrinServerConnection() {
            @Override
            public Observable<DoctrinMessageWrapper> getMessages() {
                return null;
            }

            @Override
            public void sendMessage(JSONObject jsonObject) {

            }

            @Override
            public Observable<String> getRemoteName() {
                return null;
            }

            @Override
            public void setRemoteName(String remoteName) {

            }
        }, new DoctrinServerApplicationImpl(new DoctrinCoreApplicationImpl(subscriptionManager)));

        NioServerSocketChannel channel = new NioServerSocketChannel();
        doctrinServerChannelInitializer.initChannel(channel);

        fail();
    }
}