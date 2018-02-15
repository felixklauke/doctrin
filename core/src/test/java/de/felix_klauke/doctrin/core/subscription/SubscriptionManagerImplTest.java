package de.felix_klauke.doctrin.core.subscription;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class SubscriptionManagerImplTest {

    private static final Subscriber TEST_SUBSCRIBER = new Subscriber() {
    };
    private SubscriptionManagerImpl subscriptionManager;

    @Before
    public void setUp() {
        Map<String, Set<Subscriber>> subscriptions = Maps.newConcurrentMap();
        Set<Subscriber> subscribers = Sets.newConcurrentHashSet();
        subscribers.add(TEST_SUBSCRIBER);
        subscriptions.put("test", subscribers);

        subscriptionManager = new SubscriptionManagerImpl(subscriptions);
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

        subscriptionManager.addSubscription("test", new Subscriber() {
        });

        assertEquals(2, subscriptionManager.getSubscriptions("test").length);
        assertEquals(1, subscriptionManager.getChannelCount());
    }

    @Test
    public void removeSubscription() {
        assertEquals(1, subscriptionManager.getChannelCount());
        assertEquals(1, subscriptionManager.getSubscriptions("test").length);

        subscriptionManager.removeSubscription("test", TEST_SUBSCRIBER);

        assertEquals(0, subscriptionManager.getSubscriptions("test").length);
        assertEquals(0, subscriptionManager.getChannelCount());
    }

    @Test
    public void removeSubscriptions() {
        subscriptionManager.removeSubscriptions(TEST_SUBSCRIBER);

        assertEquals(0, subscriptionManager.getSubscriptions("test").length);
        assertEquals(0, subscriptionManager.getChannelCount());
    }

    @Test
    public void getSubscriptions() {
        Subscriber[] subscriptions = subscriptionManager.getSubscriptions("test");

        assertEquals(1, subscriptions.length);
    }
}