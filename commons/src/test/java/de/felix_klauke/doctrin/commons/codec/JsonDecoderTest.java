package de.felix_klauke.doctrin.commons.codec;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class JsonDecoderTest {

    private static final ByteBuf TEST_BUFFER = Unpooled.buffer();
    private static final JSONObject TEST_CONTENT = new JSONObject().put("name", "felix").put("age", 19);

    private JsonDecoder jsonDecoder;

    @Before
    public void setUp() {
        jsonDecoder = new JsonDecoder();

        JsonEncoder jsonEncoder = new JsonEncoder();
        jsonEncoder.encode(null, TEST_CONTENT, TEST_BUFFER);
    }

    @Test
    public void decode() {
        List<Object> objects = Lists.newArrayList();

        try {
            jsonDecoder.decode(null, TEST_BUFFER, objects);
        } catch (Exception e) {
            Assert.fail();
        }

        assertEquals(objects.size(), 1);
        Object o = objects.get(0);

        assertTrue(o instanceof JSONObject);
        assertTrue(TEST_CONTENT.toString().equals(o.toString()));
    }
}