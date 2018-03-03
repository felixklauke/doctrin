package de.felix_klauke.doctrin.commons.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.json.JSONObject;

import java.util.List;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class JsonDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        // Discard length
        int i = in.readInt();

        int contentLength = in.readInt();
        byte[] content = new byte[contentLength];
        in.readBytes(content);

        JSONObject jsonObject = new JSONObject(new String(content).trim());
        out.add(jsonObject);
    }
}
