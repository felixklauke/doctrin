package de.felix_klauke.doctrin.server.module;

import com.google.inject.AbstractModule;
import de.felix_klauke.doctrin.server.DoctrinServerApplication;
import de.felix_klauke.doctrin.server.DoctrinServerApplicationImpl;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinServerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DoctrinServerApplication.class).to(DoctrinServerApplicationImpl.class).asEagerSingleton();
    }
}
