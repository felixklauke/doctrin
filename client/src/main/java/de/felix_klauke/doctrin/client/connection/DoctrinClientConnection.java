package de.felix_klauke.doctrin.client.connection;

import io.reactivex.Observable;
import org.json.JSONObject;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public interface DoctrinClientConnection {

    /**
     * Get the observable of the messages.
     *
     * @return The observable messages.
     */
    Observable<JSONObject> getMessages();

    /**
     * Get the observable of the connection state.
     *
     * @return The connection state.
     */
    Observable<Boolean> getConnected();

    /**
     * Send the given message to the server.
     *
     * @param jsonObject The json object.
     */
    void sendMessage(JSONObject jsonObject);
}
