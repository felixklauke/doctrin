package de.felix_klauke.doctrin.server.config;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test for the {@link DoctrinServerConfig}.
 *
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinServerConfigTest {

    private static final String TEST_HOST = "localhost";
    private static final int TEST_PORT = 8089;

    private DoctrinServerConfig doctrinServerConfig;

    @Before
    public void setUp() {
        doctrinServerConfig = new DoctrinServerConfig(TEST_HOST, TEST_PORT, 1, 4);
    }

    @Test
    public void getPort() {
        assertEquals(TEST_HOST, doctrinServerConfig.getHost());
    }

    @Test
    public void getHost() {
        assertEquals(TEST_PORT, doctrinServerConfig.getPort());
    }
}