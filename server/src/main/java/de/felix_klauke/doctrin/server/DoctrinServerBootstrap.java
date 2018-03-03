package de.felix_klauke.doctrin.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.felix_klauke.doctrin.server.config.DoctrinServerConfig;
import de.felix_klauke.doctrin.server.module.DoctrinServerModule;

/**
 * The main bootstrap class.
 *
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinServerBootstrap {

    public DoctrinServerBootstrap() {
        throw new AssertionError("This bootstrap should never be initialized.");
    }

    /**
     * The entry method of the JVM.
     *
     * @param args The cli arguments.
     */
    public static void main(String[] args) {
        DoctrinServerConfig doctrinServerConfig = new DoctrinServerConfig("localhost", 8085, 1, 4);

        Injector injector = Guice.createInjector(new DoctrinServerModule(doctrinServerConfig));
        DoctrinServerApplication application = injector.getInstance(DoctrinServerApplication.class);
        application.initialize();
    }
}
