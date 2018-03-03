package de.felix_klauke.doctrin.core.subscription;

import de.felix_klauke.doctrin.commons.message.DoctrinMessageContext;
import org.json.JSONObject;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriberImpl)) {
            return false;
        }
        SubscriberImpl that = (SubscriberImpl) o;
        return Objects.equals(lastMessageContext.getRemoteName(), that.lastMessageContext.getRemoteName());
    }
}
