package de.felix_klauke.doctrin.core.subscription;

import de.felix_klauke.doctrin.commons.message.DoctrinMessageContext;
import org.json.JSONObject;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public interface Subscriber {

    /**
     * Send the given object to the client.
     *
     * @param jsonObject The json object.
     */
    void sendObject(JSONObject jsonObject);

    /**
     * Set the context of the last message of this client.
     *
     * @param messageContext The message context.
     */
    void setLastMessageContext(DoctrinMessageContext messageContext);
}
