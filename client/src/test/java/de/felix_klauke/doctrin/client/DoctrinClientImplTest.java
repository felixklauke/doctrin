package de.felix_klauke.doctrin.client;

import de.felix_klauke.doctrin.client.exception.NoSuchSubscriptionException;
import de.felix_klauke.doctrin.client.net.NetworkClient;
import io.reactivex.Observable;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
@RunWith(MockitoJUnitRunner.class)
public class DoctrinClientImplTest {

    private DoctrinClientImpl doctrinClient;

    @Mock
    NetworkClient networkClient;

    @Before
    public void setUp() {
        doctrinClient = new DoctrinClientImpl(networkClient);
    }

    @Test
    public void testSubscribeChannel() {
        Observable<JSONObject> observable = doctrinClient.subscribeChannel("test");

        assertNotNull(observable);
    }

    @Test
    public void testSubscribeChannelWithCacheHit() {
        Observable<JSONObject> observable = doctrinClient.subscribeChannel("test");
        Observable<JSONObject> test = doctrinClient.subscribeChannel("test");

        assertEquals(observable, test);
    }

    @Test
    public void testUnsubscribe() {
        Observable<JSONObject> observable = doctrinClient.subscribeChannel("test");

        CountDownLatch countDownLatch = new CountDownLatch(1);

        observable.doOnComplete(countDownLatch::countDown);

        doctrinClient.unsubscribeChannel("test");

        try {
            countDownLatch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = NoSuchSubscriptionException.class)
    public void testUnsubscribeWithException() {
        doctrinClient.unsubscribeChannel("test");
    }
}