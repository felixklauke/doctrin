package de.felix_klauke.doctrin.server.network;

/**
 * Controller for the lifecycle of a server.
 *
 * @author Felix Klauke <fklauke@itemis.de>
 */
public interface DoctrinServer {

    /**
     * Start the serber up.
     */
    void start();

    /**
     * Check if the server is currently running.
     *
     * @return The state of the server.
     */
    boolean isRunning();

    /**
     * Stop the server gracefully.
     */
    void stop();
}
