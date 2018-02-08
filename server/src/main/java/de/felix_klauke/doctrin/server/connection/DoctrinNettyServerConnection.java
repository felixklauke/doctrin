package de.felix_klauke.doctrin.server.connection;

import de.felix_klauke.doctrin.commons.message.DoctrinMessage;
import de.felix_klauke.doctrin.commons.message.DoctrinMessageContext;
import de.felix_klauke.doctrin.commons.message.DoctrinMessageWrapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import org.json.JSONObject;

import java.util.UUID;

/**
 * The connection to the a client. It represents the last part in the {@link ChannelPipeline} and will handle the
 * instances of {@link JSONObject} coming in. To handle the objects they will be emitted into the {@link #publishSubject}.
 * As soon as the connection becomes active (what is monitored by {@link #channelActive(ChannelHandlerContext)}
 * the {@link Channel} will be stored and used for all further communication. As soon as the connection becomes inactive
 * (what is monitored by {@link #channelInactive(ChannelHandlerContext)}) the {@link #publishSubject} will be marked
 * as completed.
 * <p>
 * The same way the life cycle events of the connection are handled (active and inactive) we will emit all failures
 * that reach the handler into the {@link #publishSubject}. The exceptions are handled by
 * {@link #exceptionCaught(ChannelHandlerContext, Throwable)} and will be emitted using
 * {@link PublishSubject#onError(Throwable)} with the exact {@link Throwable}.
 * <p>
 * If you want to use the connection for sending a {@link JSONObject} you have to use the
 * {@link #sendMessage(JSONObject)}.
 * <p>
 * When you want to obtain the sequence of messages that comes out of a connection you will want to subscribe
 * to the {@link #getMessages()} observable that is fed by the underlying {@link #publishSubject}.
 *
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinNettyServerConnection extends SimpleChannelInboundHandler<JSONObject> implements DoctrinServerConnection {

    /**
     * The publish subject that will manage the sequence of incoming messages (Remember: Only the incoming messages
     * will be emitted into this subject, the outgoing will just pass through without this subject noticing at least
     * anything).
     */
    private final PublishSubject<DoctrinMessageWrapper> publishSubject = PublishSubject.create();

    /**
     * The subject of the remotes name.
     */
    private final BehaviorSubject<String> remoteNameSubject = BehaviorSubject.createDefault(UUID.randomUUID().toString());

    /**
     * The Netty channel that represents the raw connection to the client. This should be assigned when the channel
     * becomes active.
     */
    private Channel channel;

    /**
     * The name of the remote.
     */
    private String remoteName = UUID.randomUUID().toString();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JSONObject msg) {
        DoctrinMessageContext doctrinMessageContext = new DoctrinNettyMessageContext(ctx, this);
        DoctrinMessage doctrinMessage = new DoctrinMessage(msg);

        DoctrinMessageWrapper doctrinMessageWrapper = new DoctrinMessageWrapper(doctrinMessage, doctrinMessageContext);
        publishSubject.onNext(doctrinMessageWrapper);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        publishSubject.onComplete();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        publishSubject.onError(cause);
    }

    @Override
    public Observable<DoctrinMessageWrapper> getMessages() {
        return publishSubject;
    }

    @Override
    public void sendMessage(JSONObject jsonObject) {
        channel.writeAndFlush(jsonObject);
    }

    @Override
    public Observable<String> getRemoteName() {
        return remoteNameSubject;
    }

    @Override
    public void setRemoteName(String remoteName) {
        remoteNameSubject.onNext(remoteName);
    }
}
