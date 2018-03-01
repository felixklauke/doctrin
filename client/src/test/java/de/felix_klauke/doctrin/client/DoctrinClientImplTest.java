package de.felix_klauke.doctrin.client;

import io.reactivex.Observable;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinClientImplTest {

    private DoctrinClientImpl doctrinClient;

    @Before
    public void setUp() {
        doctrinClient = new DoctrinClientImpl();
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
}