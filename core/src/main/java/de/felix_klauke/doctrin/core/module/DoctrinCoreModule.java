package de.felix_klauke.doctrin.core.module;

import com.google.inject.AbstractModule;
import de.felix_klauke.doctrin.core.DoctrinCoreApplication;
import de.felix_klauke.doctrin.core.DoctrinCoreApplicationImpl;
import de.felix_klauke.doctrin.core.subscription.SubscriptionManager;
import de.felix_klauke.doctrin.core.subscription.SubscriptionManagerImpl;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinCoreModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DoctrinCoreApplication.class).to(DoctrinCoreApplicationImpl.class).asEagerSingleton();
        bind(SubscriptionManager.class).to(SubscriptionManagerImpl.class).asEagerSingleton();
    }
}
