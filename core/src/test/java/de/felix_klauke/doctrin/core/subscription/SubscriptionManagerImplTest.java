package de.felix_klauke.doctrin.core.subscription;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
@RunWith(MockitoJUnitRunner.class)
public class SubscriptionManagerImplTest {

    @Mock
    private Subscriber testSubscriber;
    private SubscriptionManagerImpl subscriptionManager;

    @Before
    public void setUp() {
        subscriptionManager = new SubscriptionManagerImpl();
        subscriptionManager.addSubscription("test", testSubscriber);
    }

    @Test
    public void testNonArgsConstructor() {
        subscriptionManager = new SubscriptionManagerImpl();

        assertEquals(0, subscriptionManager.getChannelCount());
        assertEquals(0, subscriptionManager.getSubscriptions("test").length);
    }

    @Test
    public void addSubscription() {
        assertEquals(1, subscriptionManager.getChannelCount());
        assertEquals(1, subscriptionManager.getSubscriptions("test").length);

        subscriptionManager.addSubscription("test", Mockito.mock(Subscriber.class));

        assertEquals(2, subscriptionManager.getSubscriptions("test").length);
        assertEquals(1, subscriptionManager.getChannelCount());
    }

    @Test
    public void removeSubscription() {
        assertEquals(1, subscriptionManager.getChannelCount());
        assertEquals(1, subscriptionManager.getSubscriptions("test").length);

        subscriptionManager.removeSubscription("test", testSubscriber);

        assertEquals(0, subscriptionManager.getSubscriptions("test").length);
        assertEquals(0, subscriptionManager.getChannelCount());
    }

    @Test
    public void removeSubscriptions() {
        subscriptionManager.removeSubscriptions(testSubscriber);

        assertEquals(0, subscriptionManager.getSubscriptions("test").length);
        assertEquals(0, subscriptionManager.getChannelCount());
    }

    @Test
    public void getSubscriptions() {
        Subscriber[] subscriptions = subscriptionManager.getSubscriptions("test");

        assertEquals(1, subscriptions.length);
    }
}