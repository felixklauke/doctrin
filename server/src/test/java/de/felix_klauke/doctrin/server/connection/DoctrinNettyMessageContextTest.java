package de.felix_klauke.doctrin.server.connection;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
@RunWith(MockitoJUnitRunner.class)
public class DoctrinNettyMessageContextTest {

    @Mock
    DoctrinNettyServerConnection doctrinServerConnection;

    private DoctrinNettyMessageContext doctrinNettyMessageContext;

    @Before
    public void setUp() {
        doctrinNettyMessageContext = new DoctrinNettyMessageContext(null, doctrinServerConnection);
    }

    @Test
    public void resume() {
        // Given
        JSONObject jsonObject = new JSONObject().put("felix", "klauke");

        // When

        doctrinNettyMessageContext.resume(jsonObject);

        // Then
        Mockito.verify(doctrinServerConnection).sendMessage(jsonObject);
    }
}