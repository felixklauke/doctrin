package de.d3adspace.doctrin.core.subscriber;

import java.util.List;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public interface SubscriptionManager {

    /**
     * Let the given subscriber subscribe to the given channel.
     *
     * @param subscriber  The subscriber.
     * @param channelName The name of the channel.
     */
    void subscribeChannel(Subscriber subscriber, String channelName);

    /**
     * Let the given subscriber unsubscribe from the given channel.
     *
     * @param subscriber  The subscriber.
     * @param channelName The name of the channel.
     */
    void unsubscribeChannel(Subscriber subscriber, String channelName);

    /**
     * Get all subscribers of the given channel.
     *
     * @param channelName The name of the channel.
     *
     * @return The subscribers.
     */
    List<Subscriber> getSubscribers(String channelName);

    /**
     * Get all subscriptions of the given subscriber.
     *
     * @param subscriber The subscriber.
     *
     * @return The subscription.
     */
    List<String> getSubscriptions(Subscriber subscriber);

    /**
     * Let the given subscriber unsubscribe from all channels.
     *
     * @param subscriber The subscriber.
     */
    void unsubscribeChannels(Subscriber subscriber);
}
