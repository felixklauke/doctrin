package de.felix_klauke.doctrin.client.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinClientConnectionImpl extends SimpleChannelInboundHandler<JSONObject> implements DoctrinClientConnection {

    private final Logger logger = LoggerFactory.getLogger(DoctrinClientConnectionImpl.class);

    /**
     * The publish subject that will emit all the messages.
     */
    private final PublishSubject<JSONObject> publishSubject = PublishSubject.create();

    /**
     * The connected subject that will emit whenever the connection state changes.
     */
    private final PublishSubject<Boolean> connectedSubject = PublishSubject.create();

    private Channel lastChannel;

    public DoctrinClientConnectionImpl(Channel lastChannel) {
        this.lastChannel = lastChannel;
    }

    @Override
    public Observable<JSONObject> getMessages() {
        return publishSubject;
    }

    @Override
    public Observable<Boolean> getConnected() {
        return connectedSubject;
    }

    @Override
    public void sendMessage(JSONObject jsonObject) {
        logger.debug("Sending object to server: {}.", jsonObject);
        lastChannel.writeAndFlush(jsonObject);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        connectedSubject.onNext(true);
        lastChannel = ctx.channel();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        connectedSubject.onNext(false);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        connectedSubject.onError(cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JSONObject msg) {
        logger.debug("Received raw message: {}.", msg);
        lastChannel = ctx.channel();
        publishSubject.onNext(msg);
    }
}
