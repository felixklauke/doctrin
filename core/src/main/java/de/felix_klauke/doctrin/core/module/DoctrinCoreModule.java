package de.felix_klauke.doctrin.core.module;

import com.google.inject.AbstractModule;
import de.felix_klauke.doctrin.core.DoctrinCoreApplication;
import de.felix_klauke.doctrin.core.DoctrinCoreApplicationImpl;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinCoreModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DoctrinCoreApplication.class).to(DoctrinCoreApplicationImpl.class).asEagerSingleton();
    }
}
