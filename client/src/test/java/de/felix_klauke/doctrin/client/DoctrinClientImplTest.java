package de.felix_klauke.doctrin.client;

import org.junit.Before;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinClientImplTest {

    private DoctrinClientImpl doctrinClient;

    @Before
    public void setUp() {
        doctrinClient = new DoctrinClientImpl();
    }
}