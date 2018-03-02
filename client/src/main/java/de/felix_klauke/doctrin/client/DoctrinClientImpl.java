package de.felix_klauke.doctrin.client;

import com.google.common.collect.Maps;
import de.felix_klauke.doctrin.client.exception.NoSuchSubscriptionException;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import org.json.JSONObject;

import java.util.Map;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinClientImpl implements DoctrinClient {

    /**
     * The storage for all active subscriptions. Will be used as a cache for duplication reasons.
     */
    private final Map<String, PublishSubject<JSONObject>> subscriptions = Maps.newConcurrentMap();

    @Override
    public Observable<JSONObject> subscribeChannel(String channelName) {
        Observable<JSONObject> subscription = subscriptions.get(channelName);

        if (subscription != null) {
            return subscription;
        }

        PublishSubject<JSONObject> subject = PublishSubject.create();
        subscriptions.put(channelName, subject);

        subject.doOnComplete(() -> subscriptions.remove(channelName));

        return subject;
    }

    @Override
    public void unsubscribeChannel(String channelName) {
        PublishSubject<JSONObject> subject = subscriptions.get(channelName);

        if (subject == null) {
            throw new NoSuchSubscriptionException("No subscription on channel " + channelName + ".");
        }

        subject.onComplete();
    }
}
