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
     * The size of the netty boss group.
     */
    private final int bossGroupSize;

    /**
     * The size of the netty worker group.
     */
    private final int workerGroupSize;

    /**
     * Create a new doctrin server config by its underlying data values.
     *
     * @param host The host.
     * @param port The port.
     * @param bossGroupSize The size of the netty boss group.
     * @param workerGroupSize The size of the netty worker group.
     */
    public DoctrinServerConfig(String host, int port, int bossGroupSize, int workerGroupSize) {
        this.host = host;
        this.port = port;
        this.bossGroupSize = bossGroupSize;
        this.workerGroupSize = workerGroupSize;
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

    /**
     * Get the size of the netty boss group.
     *
     * @return The size of the netty boss group.
     */
    public int getBossGroupSize() {
        return bossGroupSize;
    }

    /**
     * Get the size of the netty worker group.
     *
     * @return The size of the netty worker group.
     */
    public int getWorkerGroupSize() {
        return workerGroupSize;
    }
}
