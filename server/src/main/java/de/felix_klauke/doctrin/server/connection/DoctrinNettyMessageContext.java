package de.felix_klauke.doctrin.server.connection;

import de.felix_klauke.doctrin.commons.message.DoctrinMessageContext;
import io.netty.channel.ChannelHandlerContext;
import org.json.JSONObject;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinNettyMessageContext implements DoctrinMessageContext {

    private final ChannelHandlerContext ctx;
    private final DoctrinNettyServerConnection doctrinNettyServerConnection;

    public DoctrinNettyMessageContext(ChannelHandlerContext ctx, DoctrinNettyServerConnection doctrinNettyServerConnection) {
        this.ctx = ctx;
        this.doctrinNettyServerConnection = doctrinNettyServerConnection;
    }

    @Override
    public void sendObject(JSONObject jsonObject) {
        doctrinNettyServerConnection.sendMessage(jsonObject);
    }

    @Override
    public String getRemoteName() {
        return doctrinNettyServerConnection.getRemoteName().blockingLast();
    }
}
