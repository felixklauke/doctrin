package de.felix_klauke.doctrin.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinServerBootstrapTest {

    @Before
    public void setUp() {
    }

    @Test
    public void main() {
        DoctrinServerBootstrap.main(new String[]{});
    }

    @After
    public void tearDown() {
    }
}