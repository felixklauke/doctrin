package de.felix_klauke.doctrin.client;

import de.felix_klauke.doctrin.client.net.NetworkClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinClientFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctrinClientFactory.class);

    public static DoctrinClient createClient(String host, int port) {
        LOGGER.info("Creating new client against {}:{}.", host, port);

        return new DoctrinClientImpl(new NetworkClientImpl(host, port));
    }
}
