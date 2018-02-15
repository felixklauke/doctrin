package de.felix_klauke.doctrin.commons.message;

import org.json.JSONObject;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public interface DoctrinMessageContext {

    /**
     * Send the given object to the client.
     *
     * @param jsonObject The object.
     */
    void sendObject(JSONObject jsonObject);

    /**
     * Get the name of the remote.
     *
     * @return The name.
     */
    String getRemoteName();
}
