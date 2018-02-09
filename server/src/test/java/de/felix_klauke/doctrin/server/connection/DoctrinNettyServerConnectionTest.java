package de.felix_klauke.doctrin.server.connection;

import de.felix_klauke.doctrin.commons.message.DoctrinMessageWrapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.reactivex.Observable;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
@RunWith(MockitoJUnitRunner.class)
public class DoctrinNettyServerConnectionTest {

    @Mock
    ChannelHandlerContext channelHandlerContext;
    @Mock
    Channel channel;

    private DoctrinNettyServerConnection doctrinNettyServerConnection;

    @Before
    public void setUp() {
        doctrinNettyServerConnection = new DoctrinNettyServerConnection();

        Mockito.when(channelHandlerContext.channel()).thenReturn(channel);
    }

    @Test
    public void getMessages() {
        Observable<DoctrinMessageWrapper> messages = doctrinNettyServerConnection.getMessages();

        assertNotNull(messages);
    }

    @Test()
    public void sendMessage() {
        // Given
        JSONObject jsonObject = new JSONObject().put("felix", "klauke");

        // When
        doctrinNettyServerConnection.channelActive(channelHandlerContext);

        doctrinNettyServerConnection.sendMessage(jsonObject);

        // Then
        Mockito.verify(channel).writeAndFlush(jsonObject);
    }

    @Test
    public void channelActive() {
        doctrinNettyServerConnection.channelActive(channelHandlerContext);
    }

    @Test
    public void channelInactive() {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        doctrinNettyServerConnection.getMessages().subscribe((msg) -> {
                }, Throwable::printStackTrace,
                countDownLatch::countDown);

        doctrinNettyServerConnection.channelInactive(channelHandlerContext);

        try {
            countDownLatch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void exceptionCaught() {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        doctrinNettyServerConnection.getMessages().subscribe((msg) -> {
        }, throwable -> {
            countDownLatch.countDown();
        });

        doctrinNettyServerConnection.exceptionCaught(channelHandlerContext, new Throwable());

        try {
            countDownLatch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void channelRead() {
        JSONObject jsonObject = new JSONObject().put("felix", "klauke");

        CountDownLatch countDownLatch = new CountDownLatch(1);

        doctrinNettyServerConnection.getMessages().subscribe(doctrinMessageWrapper -> {
            assertEquals(jsonObject, doctrinMessageWrapper.getMessage().getJsonObject());

            countDownLatch.countDown();
        });

        doctrinNettyServerConnection.channelRead0(channelHandlerContext, jsonObject);

        try {
            countDownLatch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void setRemoteName() {
        assertNotNull(doctrinNettyServerConnection.getRemoteName());

        doctrinNettyServerConnection.setRemoteName("test-01");

        assertNotNull(doctrinNettyServerConnection.getRemoteName());
        assertEquals("test-01", doctrinNettyServerConnection.getRemoteName().blockingFirst());
    }
}