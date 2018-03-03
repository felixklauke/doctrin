package de.felix_klauke.doctrin.client;

import com.google.common.collect.Maps;
import de.felix_klauke.doctrin.client.exception.NoSuchSubscriptionException;
import de.felix_klauke.doctrin.client.net.NetworkClient;
import de.felix_klauke.doctrin.commons.exception.MissingTargetChannelException;
import de.felix_klauke.doctrin.commons.message.ActionCode;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinClientImpl implements DoctrinClient {

    /**
     * The storage for all active subscriptions. Will be used as a cache for duplication reasons.
     */
    private final Map<String, PublishSubject<JSONObject>> subscriptions = Maps.newConcurrentMap();

    /**
     * The network client for connection with the doctrin server.
     */
    private final NetworkClient networkClient;

    /**
     * The subscription of the messages that come from the client.
     */
    private Disposable messageSubscription;

    public DoctrinClientImpl(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }

    @Override
    public Observable<Boolean> connect() {
        Observable<Boolean> connectObservable = networkClient.connect().retryWhen(throwableObservable ->
                throwableObservable.flatMap(throwable -> {
                            if (throwable instanceof IOException) {
                                System.out.println("Connecting failed: " + throwable.getMessage() + " Retrying...");
                                return Observable.timer(1, TimeUnit.SECONDS);
                            }

                            return Observable.error(throwable);
                        }
                ));

        connectObservable.doOnNext(aBoolean -> {
            Observable<JSONObject> messages = networkClient.getMessages();
            messageSubscription = messages.subscribe(this::handleMessage);
        });

        return connectObservable;
    }

    @Override
    public void disconnect() {
        networkClient.disconnect();
        messageSubscription.dispose();
    }

    @Override
    public Observable<JSONObject> subscribeChannel(String channelName) {
        Observable<JSONObject> subscription = subscriptions.get(channelName);

        if (subscription != null) {
            return subscription;
        }

        PublishSubject<JSONObject> subject = PublishSubject.create();
        subscriptions.put(channelName, subject);

        networkClient.sendMessage(new JSONObject()
                .put("actionCode", ActionCode.SUBSCRIBE.ordinal())
                .put("targetChannel", channelName));

        subject.doOnComplete(() -> subscriptions.remove(channelName));

        return subject;
    }

    @Override
    public void publishOther(String channel, JSONObject jsonObject) {
        jsonObject.put("targetChannel", channel);
        jsonObject.put("actionCode", ActionCode.PUBLISH_OTHER.ordinal());

        networkClient.sendMessage(jsonObject);
    }

    @Override
    public void unsubscribeChannel(String channelName) {
        PublishSubject<JSONObject> subject = subscriptions.get(channelName);

        if (subject == null) {
            throw new NoSuchSubscriptionException("No subscription on channel " + channelName + ".");
        }

        subject.onComplete();
    }

    @Override
    public void publish(String channel, JSONObject jsonObject) {
        jsonObject.put("targetChannel", channel);
        jsonObject.put("actionCode", ActionCode.PUBLISH.ordinal());

        networkClient.sendMessage(jsonObject);
    }

    /**
     * Handle the given incoming message.
     *
     * @param jsonObject The json object.
     */
    private void handleMessage(JSONObject jsonObject) {
        String channel = (String) jsonObject.remove("targetChannel");

        if (channel == null) {
            throw new MissingTargetChannelException("The message " + jsonObject + " misses target channel field.");
        }

        PublishSubject<JSONObject> subject = subscriptions.get(channel);

        if (subject == null) {
            return;
        }

        subject.onNext(jsonObject);
    }
}
