package de.felix_klauke.doctrin.client.net;

import io.reactivex.Observable;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class NetworkClientImplTest {

    private NetworkClientImpl networkClient;

    @Before
    public void setUp() {
        networkClient = new NetworkClientImpl("0.0.0.0", 25569);
    }

    @Test(expected = Exception.class)
    public void connect() {
        Boolean aBoolean = networkClient.connect().blockingFirst();
    }

    @Test
    public void isConnected() {
        boolean connected = networkClient.isConnected();
        assertFalse(connected);
    }

    @Test
    public void disconnect() {

    }

    @Test
    public void sendMessage() {
        JSONObject mock = Mockito.mock(JSONObject.class);
        networkClient.sendMessage(mock);
    }

    @Test
    public void getReconnect() {
        Observable<Boolean> reconnect = networkClient.getReconnect();
        assertNotNull(reconnect);
    }

    @Test
    public void getMessages() {
        Observable<JSONObject> messages = networkClient.getMessages();
        assertNotNull(messages);
    }
}