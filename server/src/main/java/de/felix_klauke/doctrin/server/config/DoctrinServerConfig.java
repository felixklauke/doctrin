package de.felix_klauke.doctrin.server.config;

/**
 * The central server config providing the most important information about the server. That includes the exposed
 * address how the server can be accessed as well as some sensitive settings that can affect performance and
 * scaling of the server.
 *
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinServerConfig {

    /**
     * The host used to access the server. Together with the {@link #port} it creates an inet address.
     */
    private final String host;

    /**
     * The port the server will be bound on.
     */
    private final int port;

    /**
     * Create a new doctrin server config by its underlying data values.
     *
     * @param host The host.
     * @param port The port.
     */
    public DoctrinServerConfig(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Get the port of the server.
     *
     * @return The port.
     */
    public int getPort() {
        return port;
    }

    /**
     * Get the host of the server.
     *
     * @return The host.
     */
    public String getHost() {
        return host;
    }
}
