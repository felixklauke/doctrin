package de.felix_klauke.doctrin.core;

import de.felix_klauke.doctrin.commons.message.ActionCode;
import de.felix_klauke.doctrin.commons.message.DoctrinMessage;
import de.felix_klauke.doctrin.commons.message.DoctrinMessageContext;
import de.felix_klauke.doctrin.core.subscription.Subscriber;
import de.felix_klauke.doctrin.core.subscription.SubscriptionManager;
import org.json.JSONObject;

import javax.inject.Inject;
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
        }
    }

    /**
     * Handle that the given subscriber wants to update its remote name.
     *
     * @param subscriber     The subscriber.
     * @param messageContext The context of the mssage.
     * @param message        The message.
     */
    private void handleMessageUpdateSubscriberName(Subscriber subscriber, DoctrinMessageContext messageContext, DoctrinMessage message) {
        JSONObject jsonObject = message.getJsonObject();
        String name = jsonObject.optString("name", UUID.randomUUID().toString());
        subscriptionManager.updateSubscriberName(subscriber, name);
        messageContext.setRemoteName(name);
    }
}
