package de.felix_klauke.doctrin.commons.codec;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.json.JSONObject;

/**
 * The maven encoder that will take our json objects and encode them into the netty {@link ByteBuf}. We have to ensure
 * that we use the right charset {@link Charsets#UTF_8}
 *
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class JsonEncoder extends MessageToByteEncoder<JSONObject> {

    @Override
    protected void encode(ChannelHandlerContext ctx, JSONObject msg, ByteBuf out) {
        String messageString = msg.toString();
        byte[] bytes = messageString.getBytes(Charsets.UTF_8);

        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
