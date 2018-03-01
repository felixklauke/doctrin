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
}
