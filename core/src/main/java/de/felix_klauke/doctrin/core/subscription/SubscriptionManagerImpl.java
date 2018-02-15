package de.felix_klauke.doctrin.core.subscription;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class SubscriptionManagerImpl implements SubscriptionManager {

    /**
     * The current active subscriptions.
     */
    private final Map<String, Set<Subscriber>> subscriptions;

    /**
     * Create a subscription manager.
     */
    public SubscriptionManagerImpl() {
        this(Maps.newConcurrentMap());
    }

    /**
     * Create a new subscription manager by its underlying subscription storage.
     *
     * @param subscriptions The subscription storage.
     */
    SubscriptionManagerImpl(Map<String, Set<Subscriber>> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @Override
    public void addSubscription(String channelName, Subscriber subscriber) {
        Set<Subscriber> subscribers = subscriptions.computeIfAbsent(channelName, k -> Sets.newConcurrentHashSet());
        subscribers.add(subscriber);
    }

    @Override
    public void removeSubscription(String channelName, Subscriber subscriber) {
        Set<Subscriber> subscribers = subscriptions.computeIfAbsent(channelName, k -> Sets.newConcurrentHashSet());
        subscribers.remove(subscriber);

        if (subscribers.size() == 0) {
            subscriptions.remove(channelName);
        }
    }

    @Override
    public void removeSubscriptions(Subscriber subscriber) {
        subscriptions.forEach((key, value) -> {
            value.remove(subscriber);

            if (value.size() == 0) {
                subscriptions.remove(key);
            }
        });
    }

    @Override
    public Subscriber[] getSubscriptions(String channelName) {
        Set<Subscriber> subscribers = subscriptions.get(channelName);

        if (subscribers == null) {
            return new Subscriber[0];
        }

        return subscribers.toArray(new Subscriber[subscribers.size()]);
    }

    @Override
    public int getChannelCount() {
        return subscriptions.size();
    }
}
