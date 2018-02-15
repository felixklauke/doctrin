package de.felix_klauke.doctrin.commons.message;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
@RunWith(MockitoJUnitRunner.class)
public class DoctrinMessageWrapperTest {

    private DoctrinMessage doctrinMessage = new DoctrinMessage(new JSONObject().put("name", "xilef"));
    @Mock
    private DoctrinMessageContext doctrinMessageContext;

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