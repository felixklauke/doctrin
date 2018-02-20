package de.felix_klauke.doctrin.core;

import de.felix_klauke.doctrin.commons.message.ActionCode;
import de.felix_klauke.doctrin.commons.message.DoctrinMessage;
import de.felix_klauke.doctrin.commons.message.DoctrinMessageContext;
import de.felix_klauke.doctrin.core.subscription.Subscriber;
import de.felix_klauke.doctrin.core.subscription.SubscriptionManager;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinCoreApplicationImpl implements DoctrinCoreApplication {

    /**
     * The manager of all subscriptions.
     */
    private final SubscriptionManager subscriptionManager;

    @Inject
    public DoctrinCoreApplicationImpl(SubscriptionManager subscriptionManager) {
        this.subscriptionManager = subscriptionManager;
    }

    @Override
    public void handleMessage(DoctrinMessageContext messageContext, DoctrinMessage message) {
        String remoteName = messageContext.getRemoteName();
        ActionCode actionCode = message.getActionCode();

        Subscriber subscriber = subscriptionManager.getSubscriber(remoteName);

        if (subscriber == null) {
            subscriber = subscriptionManager.createSubscriber(messageContext, remoteName);
        }

        subscriber.setLastMessageContext(messageContext);

        switch (actionCode) {
            case UPDATE_SUBSCRIBER_NAME: {
                handleMessageUpdateSubscriberName(subscriber, messageContext, message);
                break;
            }
            case SUBSCRIBE: {
                handleMessageSubscribe(subscriber, message);
                break;
            }
            case UNSUBSCRIBE: {
                handleMessageUnsubscribe(subscriber, message);
                break;
            }
            case PUBLISH: {
                handleMessagePublish(subscriber, message);
            }
        }
    }

    /**
     * Handle that the given subscriber wants to publish something.
     *
     * @param subscriber The subscriber.
     * @param message    The message.
     */
    private void handleMessagePublish(Subscriber subscriber, DoctrinMessage message) {
        String channelName = String.valueOf(message.getJsonObject().remove("targetChannel"));
        Subscriber[] subscriptions = subscriptionManager.getSubscriptions(channelName);

        Arrays.stream(subscriptions).filter(subscription -> subscriber != subscription).forEach(subscription -> subscription.sendObject(message.getJsonObject()));
    }

    /**
     * Handle that the given subscriber wants to unsubscribe from a channel.
     *
     * @param subscriber The subscriber.
     * @param message    The message.
     */
    private void handleMessageUnsubscribe(Subscriber subscriber, DoctrinMessage message) {
        String channelName = String.valueOf(message.getJsonObject().remove("targetChannel"));
        subscriptionManager.removeSubscription(channelName, subscriber);
    }

    /**
     * Handle that the given subscriber wants to subscribe to a channel.
     *
     * @param subscriber The subscriber.
     * @param message    The message,
     */
    private void handleMessageSubscribe(Subscriber subscriber, DoctrinMessage message) {
        String channelName = String.valueOf(message.getJsonObject().remove("targetChannel"));
        subscriptionManager.addSubscription(channelName, subscriber);
    }

    /**
     * Handle that the given subscriber wants to update its remote name.
     *
     * @param subscriber     The subscriber.
     * @param messageContext The context of the message.
     * @param message        The message.
     */
    private void handleMessageUpdateSubscriberName(Subscriber subscriber, DoctrinMessageContext messageContext, DoctrinMessage message) {
        JSONObject jsonObject = message.getJsonObject();
        String name = jsonObject.optString("name", UUID.randomUUID().toString());
        subscriptionManager.updateSubscriberName(subscriber, name);
        messageContext.setRemoteName(name);
    }
}
