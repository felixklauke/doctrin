package de.d3adspace.doctrin.core.subscriber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
@ExtendWith(MockitoExtension.class)
class SubscriptionManagerImplTest {

  private static final String TEST_CHANNEL0 = "TestChannel0";
  private static final String TEST_CHANNEL1 = "TestChannel1";

  @Mock
  private Subscriber testSubscriber0;
  @Mock
  private Subscriber testSubscriber1;

  private Map<String, List<Subscriber>> subscriptionsBackend;
  private SubscriptionManagerImpl subscriptionManager;

  @BeforeEach
  void setUp() {

    subscriptionsBackend = Maps.newHashMap();
    subscriptionsBackend.put(TEST_CHANNEL0, Lists.newArrayList(testSubscriber0));

    subscriptionManager = new SubscriptionManagerImpl(subscriptionsBackend);
  }

  @Test
  void testSubscribeChannel() {

    subscriptionManager.subscribeChannel(testSubscriber1, TEST_CHANNEL1);

    assertTrue(subscriptionsBackend.containsKey(TEST_CHANNEL1),
      "There is no management for channel " + TEST_CHANNEL1);
    assertEquals(2, subscriptionsBackend.size(), "Size should be 2");
    assertTrue(subscriptionsBackend.get(TEST_CHANNEL1).contains(testSubscriber1),
      "Subscriber of " + TEST_CHANNEL1 + " should container subscriber.");
  }

  @Test
  void testUnsubscribeChannelZero() {

    subscriptionManager.unsubscribeChannel(testSubscriber0, TEST_CHANNEL0);

    assertEquals(0, subscriptionsBackend.size(), "Size should be 0");
  }

  @Test
  void testUnsubscribeChannelOne() {

    subscriptionManager.unsubscribeChannel(testSubscriber0, TEST_CHANNEL1);

    assertEquals(1, subscriptionsBackend.size(), "Size should be 0");
  }

  @Test
  void testGetSubscribersChannelZero() {

    List<Subscriber> subscribers = subscriptionManager.getSubscribers(TEST_CHANNEL0);

    assertEquals(1, subscribers.size(), "Size should be 1");
    assertTrue(subscribers.contains(testSubscriber0));
  }

  @Test
  void testGetSubscribersChannelOne() {

    List<Subscriber> subscribers = subscriptionManager.getSubscribers(TEST_CHANNEL1);

    assertEquals(0, subscribers.size(), "Size should be 0");
  }

  @Test
  void testGetSubscriptionsZero() {

    List<String> subscriptions = subscriptionManager.getSubscriptions(testSubscriber0);

    assertEquals(1, subscriptions.size(), "Size should be 1");
    assertEquals(TEST_CHANNEL0, subscriptions.get(0), "Subscription should be on " + TEST_CHANNEL0);
  }

  @Test
  void testGetSubscriptionsOne() {

    List<String> subscriptions = subscriptionManager.getSubscriptions(testSubscriber1);

    assertEquals(0, subscriptions.size(), "Size should be 0");
  }

  @Test
  void unsubscribeChannels() {

    subscriptionManager.unsubscribeChannels(testSubscriber0);

    assertEquals(0, subscriptionsBackend.size(), "Size should be 0");
  }
}
