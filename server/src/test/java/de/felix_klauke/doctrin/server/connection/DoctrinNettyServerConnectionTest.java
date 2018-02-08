package de.felix_klauke.doctrin.server.connection;

import de.felix_klauke.doctrin.commons.message.DoctrinMessageWrapper;
import io.reactivex.Observable;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinNettyServerConnectionTest {

    private DoctrinNettyServerConnection doctrinNettyServerConnection;

    @Before
    public void setUp() {
        doctrinNettyServerConnection = new DoctrinNettyServerConnection();
    }

    @Test
    public void getMessages() {
        Observable<DoctrinMessageWrapper> messages = doctrinNettyServerConnection.getMessages();

        assertNotNull(messages);
    }

    @Test(expected = NullPointerException.class)
    public void sendMessage() {
        doctrinNettyServerConnection.sendMessage(new JSONObject());
    }

    @Test
    public void setRemoteName() {
        assertNotNull(doctrinNettyServerConnection.getRemoteName());

        doctrinNettyServerConnection.setRemoteName("test-01");

        assertNotNull(doctrinNettyServerConnection.getRemoteName());
        assertEquals("test-01", doctrinNettyServerConnection.getRemoteName().blockingFirst());
    }
}