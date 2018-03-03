package de.felix_klauke.doctrin.client;

import io.reactivex.Observable;
import org.json.JSONObject;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public interface DoctrinClient {

    /**
     * Connect to the server.
     */
    Observable<Boolean> connect();

    /**
     * Disconnect from the server.
     */
    void disconnect();

    /**
     * Subscribe to channel with the given name.
     *
     * @param channelName The channel name.
     * @return The channel name.
     */
    Observable<JSONObject> subscribeChannel(String channelName);

    /**
     * Unsubscribe from the given channel.
     *
     * @param channelName The channel name.
     */
    void unsubscribeChannel(String channelName);

    /**
     * Publish the given json object to the given channel.
     *
     * If you subscribed to the given channel you will also receive the message.
     *
     * @param channel    The channel.
     * @param jsonObject The json object.
     */
    void publish(String channel, JSONObject jsonObject);

    /**
     * Publish the given json object to the given channel.
     * <p>
     * This client won't receive the message even if you have subscribed the given channel.
     *
     * @param channel    The channel.
     * @param jsonObject The json object.
     */
    void publishOther(String channel, JSONObject jsonObject);
}
