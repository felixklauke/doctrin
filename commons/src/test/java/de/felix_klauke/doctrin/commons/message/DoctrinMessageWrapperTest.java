package de.felix_klauke.doctrin.commons.message;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinMessageWrapperTest {

    private DoctrinMessage doctrinMessage = new DoctrinMessage(new JSONObject().put("name", "xilef"));
    private DoctrinMessageContext doctrinMessageContext = jsonObject -> {

    };

    private DoctrinMessageWrapper doctrinMessageWrapper;

    @Before
    public void setUp() {
        doctrinMessageWrapper = new DoctrinMessageWrapper(doctrinMessage, doctrinMessageContext);
    }

    @Test
    public void getMessage() {
        assertEquals(doctrinMessage, doctrinMessageWrapper.getMessage());
    }

    @Test
    public void getMessageContext() {
        assertEquals(doctrinMessageContext, doctrinMessageWrapper.getMessageContext());
    }
}