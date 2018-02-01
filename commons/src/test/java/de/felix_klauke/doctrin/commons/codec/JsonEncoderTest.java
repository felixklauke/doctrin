package de.felix_klauke.doctrin.commons.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class JsonEncoderTest {

    private static final String TEST_MESSAGE_RAW = "{\"name\":\"felix\",\"age\":19}";
    private static final JSONObject TEST_MESSAGE = new JSONObject(TEST_MESSAGE_RAW);

    private JsonEncoder jsonEncoder;

    @Before
    public void setUp() {
        jsonEncoder = new JsonEncoder();
    }

    @Test
    public void encode() {
        ByteBuf byteBuf = Unpooled.buffer();

        try {
            jsonEncoder.encode(null, TEST_MESSAGE, byteBuf);
        } catch (Exception e) {
            Assert.fail();
        }

        int resultLength = byteBuf.readInt();
        byte[] resultContent = new byte[resultLength];
        byteBuf.readBytes(resultContent);

        String result = new String(resultContent);

        assertEquals(result, TEST_MESSAGE_RAW);
    }
}