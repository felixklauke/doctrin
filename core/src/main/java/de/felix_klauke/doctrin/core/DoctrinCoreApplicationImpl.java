package de.felix_klauke.doctrin.core;

import de.felix_klauke.doctrin.commons.message.ActionCode;
import de.felix_klauke.doctrin.commons.message.DoctrinMessage;
import de.felix_klauke.doctrin.commons.message.DoctrinMessageContext;
import de.felix_klauke.doctrin.core.subscription.Subscriber;
import de.felix_klauke.doctrin.core.subscription.SubscriptionManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinCoreApplicationImpl implements DoctrinCoreApplication {

    private final Logger logger = LoggerFactory.getLogger(DoctrinCoreApplicationImpl.class);

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

        logger.info("Handling {} for remote {} and action {}.", message.getJsonObject(), remoteName, actionCode);

        Subscriber subscriber = subscriptionManager.getSubscriber(remoteName);

        if (subscriber == null) {
            logger.info("No suitable subscriber found for remote name {}.", remoteName);
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
            case BULK_SUBSCRIBE: {
                handleMessageBulkSubscribe(subscriber, message);
                break;
            }
            case UNSUBSCRIBE: {
                handleMessageUnsubscribe(subscriber, message);
                break;
            }
            case PUBLISH_OTHER: {
                handleMessagePublish(subscriber, message, false);
                break;
            }
            case PUBLISH: {
                handleMessagePublish(subscriber, message, true);
                break;
            }
        }
    }

    /**
     * Handle that a client wants to subscribe to multiple channels at once.
     *
     * @param subscriber The subscriber.
     * @param message    The message.
     */
    private void handleMessageBulkSubscribe(Subscriber subscriber, DoctrinMessage message) {
        JSONObject jsonObject = message.getJsonObject();
        JSONArray targetChannels = jsonObject.getJSONArray("targetChannels");
        List<Object> objects = targetChannels.toList();

        for (Object channelNameObject : objects) {
            if (!(channelNameObject instanceof String)) {
                continue;
            }

            subscriptionManager.addSubscription(channelNameObject.toString(), subscriber);
        }
    }

    @Override
    public void handleInactiveSubscriber(String subscriberName) {
        logger.info("Subscriber {} was marked as inactive. Removing subscriptions.", subscriberName);
        subscriptionManager.removeSubscriptions(subscriptionManager.getSubscriber(subscriberName));
    }

    /**
     * Handle that the given subscriber wants to publish something.
     *
     * @param subscriber The subscriber.
     * @param message    The message.
     * @param selfNotification If the subscriber should also get the message of he subscribed the channel.
     */
    private void handleMessagePublish(Subscriber subscriber, DoctrinMessage message, boolean selfNotification) {
        logger.info("Handling publish {} from {} with self notification {}.", message.getJsonObject(), subscriber.getName(), selfNotification);

        String channelName = String.valueOf(message.getJsonObject().remove("targetChannel"));
        Subscriber[] subscriptions = subscriptionManager.getSubscriptions(channelName);

        message.getJsonObject().put("targetChannel", channelName);

        if (selfNotification) {
            Arrays.stream(subscriptions).forEach(subscription -> subscription.sendObject(message.getJsonObject()));
            return;
        }

        Arrays.stream(subscriptions)
                .filter(subscriber1 -> !subscriber1.equals(subscriber))
                .forEach(subscription -> subscription.sendObject(message.getJsonObject()));
    }

    /**
     * Handle that the given subscriber wants to unsubscribe from a channel.
     *
     * @param subscriber The subscriber.
     * @param message    The message.
     */
    private void handleMessageUnsubscribe(Subscriber subscriber, DoctrinMessage message) {
        String channelName = String.valueOf(message.getJsonObject().remove("targetChannel"));

        logger.info("Handling that {} unsubscribes from channel {} via message {}.", subscriber.getName(), channelName, message.getJsonObject());

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

        logger.info("Handling that {} subscribes on channel {} via message {}.", subscriber.getName(), channelName, message.getJsonObject());

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

        logger.info("Handling that {} updates his name to {} via message {}.", subscriber.getName(), name, message.getJsonObject());

        subscriptionManager.updateSubscriberName(subscriber, name);
        messageContext.setRemoteName(name);
    }
}
