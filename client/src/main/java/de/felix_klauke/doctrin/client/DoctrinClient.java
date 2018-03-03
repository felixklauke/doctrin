package de.felix_klauke.doctrin.client;

import io.reactivex.Observable;
import org.json.JSONObject;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public interface DoctrinClient {

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
     * @param channel    The channel.
     * @param jsonObject the json object.
     */
    void publish(String channel, JSONObject jsonObject);
}
