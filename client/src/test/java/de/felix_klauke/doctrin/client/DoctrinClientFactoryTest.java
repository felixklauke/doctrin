package de.felix_klauke.doctrin.client;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinClientFactoryTest {

    @Test
    public void createClient() {
        DoctrinClient client = DoctrinClientFactory.createClient("0.0.0.0", 25568);
        assertNotNull(client);
    }

    @Test(expected = AssertionError.class)
    public void testInit() {
        new DoctrinClientFactory();

        Assert.fail();
    }
}