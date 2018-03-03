package de.felix_klauke.doctrin.client.channel;

import de.felix_klauke.doctrin.client.connection.DoctrinClientConnection;
import de.felix_klauke.doctrin.client.connection.DoctrinClientConnectionImpl;
import de.felix_klauke.doctrin.commons.codec.JsonDecoder;
import de.felix_klauke.doctrin.commons.codec.JsonEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinClientChannelInitializer extends ChannelInitializer<Channel> {

    /**
     * The subject that will emit all connections.
     */
    private final PublishSubject<DoctrinClientConnection> clientConnectionSubject = PublishSubject.create();

    @Override
    protected void initChannel(Channel ch) {
        ChannelPipeline channelPipeline = ch.pipeline();

        channelPipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4));
        channelPipeline.addLast(new JsonDecoder());
        channelPipeline.addLast(new LengthFieldPrepender(4));
        channelPipeline.addLast(new JsonEncoder());

        DoctrinClientConnectionImpl clientConnection = new DoctrinClientConnectionImpl(ch);
        channelPipeline.addLast(clientConnection);
        clientConnectionSubject.onNext(clientConnection);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }

    /**
     * Get the observable of new client connections.
     *
     * @return The client connections.
     */
    public Observable<DoctrinClientConnection> getClientConnections() {
        return clientConnectionSubject;
    }
}
