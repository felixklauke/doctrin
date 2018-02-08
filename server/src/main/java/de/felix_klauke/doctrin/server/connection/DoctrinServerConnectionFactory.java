package de.felix_klauke.doctrin.server.connection;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public interface DoctrinServerConnectionFactory {

    /**
     * Create a new handler for server messages.
     *
     * @return The connection.
     */
    DoctrinServerConnection createConnection();
}
