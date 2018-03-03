package de.felix_klauke.doctrin.client;

import de.felix_klauke.doctrin.client.net.NetworkClientImpl;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinClientFactory {

    public static DoctrinClient createClient(String host, int port) {
        return new DoctrinClientImpl(new NetworkClientImpl(host, port));
    }
}
