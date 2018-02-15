package de.felix_klauke.doctrin.core.subscription;

import de.felix_klauke.doctrin.commons.message.DoctrinMessageContext;
import org.json.JSONObject;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class SubscriberImpl implements Subscriber {

    /**
     * The context of the last
     */
    private DoctrinMessageContext lastMessageContext;

    /**
     * Create a new subscriber.
     *
     * @param messageContext The context of the message that created this subscriber.
     */
    public SubscriberImpl(DoctrinMessageContext messageContext) {
        lastMessageContext = messageContext;
    }

    @Override
    public void sendObject(JSONObject jsonObject) {
        lastMessageContext.sendObject(jsonObject);
    }

    @Override
    public void setLastMessageContext(DoctrinMessageContext messageContext) {
        this.lastMessageContext = messageContext;
    }
}
