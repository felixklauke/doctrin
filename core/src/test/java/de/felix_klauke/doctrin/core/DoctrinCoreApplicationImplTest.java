package de.felix_klauke.doctrin.core;

import de.felix_klauke.doctrin.commons.message.ActionCode;
import de.felix_klauke.doctrin.commons.message.DoctrinMessage;
import de.felix_klauke.doctrin.commons.message.DoctrinMessageContext;
import de.felix_klauke.doctrin.core.subscription.SubscriptionManagerImpl;
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
public class DoctrinCoreApplicationImplTest {

    @Mock
    private DoctrinMessageContext doctrinMessageContext;

    private DoctrinCoreApplicationImpl doctrinCoreApplication;

    @Before
    public void setUp() {
        doctrinCoreApplication = new DoctrinCoreApplicationImpl(new SubscriptionManagerImpl());

        Mockito.when(doctrinMessageContext.getRemoteName()).thenReturn("subscriber-01");
    }

    @Test
    public void handleMessage() {
        DoctrinMessage doctrinMessage = new DoctrinMessage(new JSONObject().put("Felix", "Klauke"));

        doctrinCoreApplication.handleMessage(doctrinMessageContext, doctrinMessage);
    }

    @Test
    public void handleMessageUpdateSubscriberName() {
        DoctrinMessage doctrinMessage = new DoctrinMessage(new JSONObject().put("Felix", "Klauke").put("actionCode", ActionCode.UPDATE_SUBSCRIBER_NAME.ordinal()));

        doctrinCoreApplication.handleMessage(doctrinMessageContext, doctrinMessage);
    }
}