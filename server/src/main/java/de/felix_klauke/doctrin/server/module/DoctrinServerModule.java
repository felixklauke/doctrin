package de.felix_klauke.doctrin.server.module;

import com.google.inject.AbstractModule;
import de.felix_klauke.doctrin.server.DoctrinServerApplication;
import de.felix_klauke.doctrin.server.DoctrinServerApplicationImpl;
import de.felix_klauke.doctrin.server.config.DoctrinServerConfig;

/**
 * The module to define or google guice dependencies.
 *
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinServerModule extends AbstractModule {

    /**
     * The config containing the central adjustments.
     */
    private final DoctrinServerConfig doctrinServerConfig;

    /**
     * Create a new doctrin server module.
     *
     * @param doctrinServerConfig The underlying server config.
     */
    public DoctrinServerModule(DoctrinServerConfig doctrinServerConfig) {
        this.doctrinServerConfig = doctrinServerConfig;
    }

    @Override
    protected void configure() {
        // Config
        bind(DoctrinServerConfig.class).toInstance(doctrinServerConfig);

        // Application
        bind(DoctrinServerApplication.class).to(DoctrinServerApplicationImpl.class).asEagerSingleton();
    }
}
