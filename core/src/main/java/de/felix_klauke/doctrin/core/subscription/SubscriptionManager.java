package de.felix_klauke.doctrin.core.subscription;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public interface SubscriptionManager {

    /**
     * Add a subscription for the given channel name by the given subscriber.
     *
     * @param channelName The name of the channel.
     * @param subscriber  The subscriber.
     */
    void addSubscription(String channelName, Subscriber subscriber);

    /**
     * Remove the subscription on the channel with the given name of the given subscriber.
     *
     * @param channelName The name of the channel.
     * @param subscriber  The subscriber.
     */
    void removeSubscription(String channelName, Subscriber subscriber);

    /**
     * Remove all subscriptions of the given subscriber.
     *
     * @param subscriber The subscriber.
     */
    void removeSubscriptions(Subscriber subscriber);

    /**
     * Get the subscriptions on a specific channel.
     *
     * @param channelName The name of the channel.
     * @return The subscriptions.
     */
    Subscriber[] getSubscriptions(String channelName);

    /**
     * Get the amount of active channels.
     *
     * @return The channel count.
     */
    int getChannelCount();
}
