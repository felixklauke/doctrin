package de.felix_klauke.doctrin.client;

import com.google.common.collect.Maps;
import de.felix_klauke.doctrin.client.exception.NoSuchSubscriptionException;
import de.felix_klauke.doctrin.client.net.NetworkClient;
import de.felix_klauke.doctrin.commons.exception.MissingTargetChannelException;
import de.felix_klauke.doctrin.commons.message.ActionCode;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinClientImpl implements DoctrinClient {

    /**
     * The logger to log all general client actions.
     */
    private final Logger logger = LoggerFactory.getLogger(DoctrinClientImpl.class);

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
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    DoctrinClientImpl(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }

    @Override
    public Observable<Boolean> connect() {
        logger.info("Connecting to the server.");

        Observable<Boolean> connectObservable = networkClient.connect().retryWhen(throwableObservable ->
                throwableObservable.flatMap(throwable -> {
                            if (throwable instanceof IOException) {
                                logger.error("Error while connecting to the server. Error was IO based: " + throwable.getMessage() + " - Reconnecting...");
                                return Observable.timer(1, TimeUnit.SECONDS);
                            }

                    logger.error("Error while connecting to the server. The error was not IO based and will be delegated to the user: " + throwable.getMessage());
                            return Observable.error(throwable);
                        }
                )).observeOn(Schedulers.io()).subscribeOn(Schedulers.io());

        resendSubscriptions();

        Observable<JSONObject> messages = networkClient.getMessages();
        compositeDisposable.add(messages.subscribe(this::handleMessage));
        return connectObservable;
    }

    /**
     * Subscribes to all channels currently subscribed in one bulk subscription.
     */
    private void resendSubscriptions() {
        networkClient.getReconnect().filter(reconnectAttemptSuccessful -> reconnectAttemptSuccessful)
                .subscribe(reconnectAttemptSuccessful -> {

                    JSONArray jsonArray = new JSONArray(subscriptions.keySet());
                    JSONObject jsonObject = new JSONObject()
                            .put("actionCode", ActionCode.BULK_SUBSCRIBE.ordinal())
                            .put("targetChannels", jsonArray);
                    networkClient.sendMessage(jsonObject);
                });
    }

    @Override
    public void disconnect() {
        logger.info("Disconnecting...");

        networkClient.disconnect();
        compositeDisposable.dispose();

        logger.info("Disconnected...");
    }

    @Override
    public Observable<JSONObject> subscribeChannel(String channelName) {
        logger.info("A subscription on the channel {} was scheduled.", channelName);

        Observable<JSONObject> subscription = subscriptions.get(channelName);

        if (subscription != null) {
            logger.info("Found existing subscription on channel {}, restoring.", channelName);
            return subscription;
        }

        PublishSubject<JSONObject> subject = PublishSubject.create();
        subscriptions.put(channelName, subject);

        sendSubscribeMessage(channelName);

        subject.doOnComplete(() -> subscriptions.remove(channelName));

        return subject;
    }

    @Override
    public void unsubscribeChannel(String channelName) {
        logger.info("Unsubscribing from channel {}.", channelName);

        PublishSubject<JSONObject> subject = subscriptions.get(channelName);

        if (subject == null) {
            throw new NoSuchSubscriptionException("No subscription on channel " + channelName + ".");
        }

        subject.onComplete();

        logger.info("Unsubscribed from channel {}.", channelName);
    }

    @Override
    public void publish(String channel, JSONObject jsonObject) {
        logger.debug("Publishing {} to channel {}.", jsonObject, channel);

        jsonObject.put("targetChannel", channel);
        jsonObject.put("actionCode", ActionCode.PUBLISH.ordinal());

        networkClient.sendMessage(jsonObject);
    }

    @Override
    public void publishOther(String channel, JSONObject jsonObject) {
        logger.debug("Publishing {} to channel {} to all other subscribers.", jsonObject, channel);

        jsonObject.put("targetChannel", channel);
        jsonObject.put("actionCode", ActionCode.PUBLISH_OTHER.ordinal());

        networkClient.sendMessage(jsonObject);
    }

    @Override
    public void setSubscriberName(String subscriberName) {
        logger.info("Updating subscriber name to {}.", subscriberName);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("actionCode", ActionCode.UPDATE_SUBSCRIBER_NAME.ordinal());
        jsonObject.put("name", subscriberName);

        networkClient.sendMessage(jsonObject);
    }

    private void sendSubscribeMessage(String channelName) {
        networkClient.sendMessage(new JSONObject()
                .put("actionCode", ActionCode.SUBSCRIBE.ordinal())
                .put("targetChannel", channelName));
    }

    /**
     * Handle the given incoming message.
     *
     * @param jsonObject The json object.
     */
    private void handleMessage(JSONObject jsonObject) {
        logger.debug("Handling incoming message {}.", jsonObject);

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
