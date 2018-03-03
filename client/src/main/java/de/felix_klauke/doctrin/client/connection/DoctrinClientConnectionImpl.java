package de.felix_klauke.doctrin.client.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import org.json.JSONObject;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinClientConnectionImpl extends SimpleChannelInboundHandler<JSONObject> implements DoctrinClientConnection {

    /**
     * The publish subject that will emit all the messages.
     */
    private final PublishSubject<JSONObject> publishSubject = PublishSubject.create();

    private Channel lastChannel;

    public DoctrinClientConnectionImpl(Channel lastChannel) {
        this.lastChannel = lastChannel;
    }

    @Override
    public Observable<JSONObject> getMessages() {
        return publishSubject;
    }

    @Override
    public void sendMessage(JSONObject jsonObject) {

        lastChannel.writeAndFlush(jsonObject);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        lastChannel = ctx.channel();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JSONObject msg) {
        lastChannel = ctx.channel();
        publishSubject.onNext(msg);
    }
}
