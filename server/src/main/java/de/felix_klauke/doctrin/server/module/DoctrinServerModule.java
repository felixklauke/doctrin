package de.felix_klauke.doctrin.server.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import de.felix_klauke.doctrin.server.DoctrinServerApplication;
import de.felix_klauke.doctrin.server.DoctrinServerApplicationImpl;
import de.felix_klauke.doctrin.server.config.DoctrinServerConfig;
import de.felix_klauke.doctrin.server.connection.DoctrinNettyServerConnection;
import de.felix_klauke.doctrin.server.connection.DoctrinServerConnection;

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

        bindConstant().annotatedWith(Names.named("serverHost")).to(doctrinServerConfig.getHost());
        bindConstant().annotatedWith(Names.named("serverPort")).to(doctrinServerConfig.getPort());

        // Application
        bind(DoctrinServerApplication.class).to(DoctrinServerApplicationImpl.class).asEagerSingleton();
      
        // Netty
        install(new DoctrinNettyServerModule(doctrinServerConfig));
    }
}
