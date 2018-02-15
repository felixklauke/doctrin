package de.felix_klauke.doctrin.core.subscription;

import de.felix_klauke.doctrin.commons.message.DoctrinMessageContext;
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
public class SubscriberImplTest {

    @Mock
    DoctrinMessageContext doctrinMessageContext;
    private SubscriberImpl subscriber;

    @Before
    public void setUp() {
        subscriber = new SubscriberImpl(doctrinMessageContext);
    }

    @Test
    public void sendObject() {
        // Given
        JSONObject jsonObject = new JSONObject();

        // When

        subscriber.sendObject(jsonObject);

        // Then
        Mockito.verify(doctrinMessageContext).sendObject(jsonObject);
    }

    @Test
    public void setLastMessageContext() {
        DoctrinMessageContext messageContext = Mockito.mock(DoctrinMessageContext.class);
        subscriber.setLastMessageContext(messageContext);

        Mockito.verifyNoMoreInteractions(doctrinMessageContext);

        // Given
        JSONObject jsonObject = new JSONObject();

        // When

        subscriber.sendObject(jsonObject);

        // Then
        Mockito.verify(messageContext).sendObject(jsonObject);
    }
}