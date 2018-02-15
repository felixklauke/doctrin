package de.felix_klauke.doctrin.commons.message;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinMessageTest {

    private final JSONObject testJsonObject = new JSONObject()
            .put("actionCode", ActionCode.UPDATE_SUBSCRIBER_NAME.ordinal())
            .put("name", "xilef");

    private DoctrinMessage doctrinMessage;

    @Before
    public void setUp() {
        doctrinMessage = new DoctrinMessage(testJsonObject);
    }

    @Test
    public void getJsonObject() {
        assertEquals(testJsonObject, doctrinMessage.getJsonObject());
    }

    @Test
    public void getActionCode() {
        ActionCode actionCode = doctrinMessage.getActionCode();

        assertEquals(ActionCode.UPDATE_SUBSCRIBER_NAME, actionCode);

        // cache hit
        actionCode = doctrinMessage.getActionCode();

        assertEquals(ActionCode.UPDATE_SUBSCRIBER_NAME, actionCode);
    }

    @Test
    public void getUnknownActionCode() {
        testJsonObject.remove("actionCode");
        ActionCode actionCode = doctrinMessage.getActionCode();

        assertEquals(ActionCode.UNKNOWN, actionCode);
    }
}