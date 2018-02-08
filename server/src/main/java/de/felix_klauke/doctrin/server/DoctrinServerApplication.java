package de.felix_klauke.doctrin.server;

import de.felix_klauke.doctrin.server.connection.DoctrinServerConnection;

/**
 * The central application interface.
 *
 * @author Felix Klauke <fklauke@itemis.de>
 */
public interface DoctrinServerApplication {

    /**
     * Initialize the server application.
     */
    void initialize();

    /**
     * Handle that a new connection was established.
     *
     * @param connection The connection.
     */
    void handleNewConnection(DoctrinServerConnection connection);
}
