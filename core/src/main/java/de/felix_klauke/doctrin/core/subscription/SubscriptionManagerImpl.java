package de.felix_klauke.doctrin.core.subscription;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import de.felix_klauke.doctrin.commons.message.DoctrinMessageContext;

import java.util.Map;
import java.util.Set;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class SubscriptionManagerImpl implements SubscriptionManager {

    /**
     * The current active subscriptions.
     */
    private final Map<String, Set<Subscriber>> subscriptions = Maps.newConcurrentMap();

    /**
     * The subscribers.
     */
    private final Map<String, Subscriber> activeSubscribers = Maps.newConcurrentMap();

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

    @Override
    public Subscriber getSubscriber(String remoteName) {
        return activeSubscribers.get(remoteName);
    }

    @Override
    public void updateSubscriberName(Subscriber subscriber, String name) {
        activeSubscribers.values().remove(subscriber);
        activeSubscribers.put(name, subscriber);
    }

    @Override
    public Subscriber createSubscriber(DoctrinMessageContext messageContext, String remoteName) {
        SubscriberImpl subscriber = new SubscriberImpl(messageContext);
        activeSubscribers.put(remoteName, subscriber);
        return subscriber;
    }
}
