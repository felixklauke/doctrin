package de.felix_klauke.doctrin.server.connection;

import io.reactivex.Observable;
import org.json.JSONObject;

/**
 * Represents the connection to a client and manages the transport layer using the methods {@link #getMessages()} and
 * {@link #sendMessage(JSONObject)} where {@link #getMessages()} represents the incoming messages and new messages
 * can be send using {@link #sendMessage(JSONObject)}.
 *
 * @author Felix Klauke <fklauke@itemis.de>
 */
public interface DoctrinServerConnection {

    /**
     * Get the sequence of messages that are coming in through the connection. Remember this doesn't include
     * outgoing messages.
     *
     * @return The observable of the messages.
     */
    Observable<JSONObject> getMessages();

    /**
     * Send a new message out in the world.
     *
     * @param jsonObject The content of the message.
     */
    void sendMessage(JSONObject jsonObject);
}
