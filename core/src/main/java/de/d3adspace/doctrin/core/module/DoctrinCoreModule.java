package de.d3adspace.doctrin.core.module;

import com.google.inject.AbstractModule;
import de.d3adspace.doctrin.core.DoctrinCore;
import de.d3adspace.doctrin.core.DoctrinCoreImpl;
import de.d3adspace.doctrin.core.subscriber.SubscriptionManager;
import de.d3adspace.doctrin.core.subscriber.SubscriptionManagerImpl;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class DoctrinCoreModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(DoctrinCore.class).to(DoctrinCoreImpl.class);
        bind(SubscriptionManager.class).to(SubscriptionManagerImpl.class);
    }
}
