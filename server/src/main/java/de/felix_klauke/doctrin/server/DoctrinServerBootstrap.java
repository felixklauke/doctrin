package de.felix_klauke.doctrin.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.felix_klauke.doctrin.server.module.DoctrinServerModule;

/**
 * The main bootstrap class.
 *
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinServerBootstrap {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new DoctrinServerModule());
        DoctrinServerApplication application = injector.getInstance(DoctrinServerApplication.class);
        application.initialize();
    }
}
