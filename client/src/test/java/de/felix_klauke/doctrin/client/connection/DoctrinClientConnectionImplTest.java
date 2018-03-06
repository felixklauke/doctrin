package de.felix_klauke.doctrin.client.connection;

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

import static org.junit.Assert.assertNotNull;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
@RunWith(MockitoJUnitRunner.class)
public class DoctrinClientConnectionImplTest {

    @Mock
    private Channel channel;
    private DoctrinClientConnectionImpl clientConnection;

    @Before
    public void setUp() {
        clientConnection = new DoctrinClientConnectionImpl(channel);
    }

    @Test
    public void getMessages() {
        Observable<JSONObject> messages = clientConnection.getMessages();

        assertNotNull(messages);
    }

    @Test
    public void getConnected() {
        Observable<Boolean> connected = clientConnection.getConnected();

        assertNotNull(connected);
    }

    @Test
    public void sendMessage() {
        JSONObject mock = Mockito.mock(JSONObject.class);

        clientConnection.sendMessage(mock);

        Mockito.verify(channel).writeAndFlush(mock);
    }

    @Test
    public void channelActive() {
        ChannelHandlerContext channelHandlerContext = Mockito.mock(ChannelHandlerContext.class);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        Observable<Boolean> messages = clientConnection.getConnected();
        messages.subscribe(connected -> countDownLatch.countDown());

        clientConnection.channelActive(channelHandlerContext);

        try {
            countDownLatch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void channelInactive() {
        ChannelHandlerContext channelHandlerContext = Mockito.mock(ChannelHandlerContext.class);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        Observable<Boolean> messages = clientConnection.getConnected();
        messages.subscribe(connected -> countDownLatch.countDown());

        clientConnection.channelInactive(channelHandlerContext);

        try {
            countDownLatch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void exceptionCaught() {
        Throwable mock = Mockito.mock(Throwable.class);
        ChannelHandlerContext channelHandlerContext = Mockito.mock(ChannelHandlerContext.class);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        Observable<Boolean> messages = clientConnection.getConnected();
        messages.subscribe(connected -> {
        }, throwable -> countDownLatch.countDown());

        clientConnection.exceptionCaught(channelHandlerContext, mock);

        try {
            countDownLatch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void channelRead0() {
        JSONObject mock = Mockito.mock(JSONObject.class);
        ChannelHandlerContext channelHandlerContext = Mockito.mock(ChannelHandlerContext.class);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        Observable<JSONObject> messages = clientConnection.getMessages();
        messages.subscribe(jsonObject -> countDownLatch.countDown());

        clientConnection.channelRead0(channelHandlerContext, mock);

        try {
            countDownLatch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}