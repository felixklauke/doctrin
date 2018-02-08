package de.felix_klauke.doctrin.commons.message;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinMessageTest {

    private static final JSONObject TEST_JSON_OBJECT = new JSONObject().put("name", "xilef");

    private DoctrinMessage doctrinMessage;

    @Before
    public void setUp() {
        doctrinMessage = new DoctrinMessage(TEST_JSON_OBJECT);
    }

    @Test
    public void getJsonObject() {
        assertEquals(TEST_JSON_OBJECT, doctrinMessage.getJsonObject());
    }
}