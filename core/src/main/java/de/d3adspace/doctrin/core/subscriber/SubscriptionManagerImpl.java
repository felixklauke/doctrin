package de.d3adspace.doctrin.core.subscriber;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class SubscriptionManagerImpl implements SubscriptionManager {

  /**
   * All currently known and active subscriptions.
   */
  private final Map<String, List<Subscriber>> subscriptions;

  SubscriptionManagerImpl(Map<String, List<Subscriber>> subscriptions) {
    this.subscriptions = subscriptions;
  }

  @Inject
  public SubscriptionManagerImpl() {
    this(Maps.newConcurrentMap());
  }

  @Override
  public void subscribeChannel(Subscriber subscriber, String channelName) {

    List<Subscriber> subscribers = subscriptions
      .computeIfAbsent(channelName, key -> Lists.newArrayList());
    subscribers.add(subscriber);
  }

  @Override
  public void unsubscribeChannel(Subscriber subscriber, String channelName) {

    List<Subscriber> subscribers = subscriptions.get(channelName);

    if (subscribers == null) {
      return;
    }

    subscribers.remove(subscriber);

    // Detect and remove empty subscriptions
    if (subscribers.isEmpty()) {
      subscriptions.remove(channelName);
    }
  }

  @Override
  public List<Subscriber> getSubscribers(String channelName) {

    List<Subscriber> subscribers = subscriptions.get(channelName);

    if (subscribers == null) {
      subscribers = Lists.newArrayList();
    }

    return Collections.unmodifiableList(subscribers);
  }

  @Override
  public List<String> getSubscriptions(Subscriber subscriber) {

    List<String> subscriberSubscriptions = Lists.newArrayList();

    // Filter subscriptions
    subscriptions.forEach((channelName, subscribers) -> {
      if (subscribers.contains(subscriber)) {
        subscriberSubscriptions.add(channelName);
      }
    });

    return subscriberSubscriptions;
  }

  @Override
  public void unsubscribeChannels(Subscriber subscriber) {

    // Channels we can delete completely as they wont have any more subscriptions
    List<String> channelsToRemove = Lists.newArrayList();

    subscriptions.forEach((channelName, subscribers) -> {
      subscribers.remove(subscriber);

      // Detect empty channel subscriptions
      if (subscribers.isEmpty()) {
        channelsToRemove.add(channelName);
      }
    });

    // Remove empty subscriptions
    subscriptions.keySet().removeAll(channelsToRemove);
  }
}
