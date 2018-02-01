package de.felix_klauke.doctrin.server;

import org.junit.Test;

/**
 * Test for {@link DoctrinServerBootstrap}.
 *
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinServerBootstrapTest {

    @Test(expected = AssertionError.class)
    public void testCreation() throws AssertionError {
        new DoctrinServerBootstrap();
    }

    @Test
    public void main() {
        DoctrinServerBootstrap.main(new String[]{});
    }
}