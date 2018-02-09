package de.felix_klauke.doctrin.core;

import de.felix_klauke.doctrin.commons.message.DoctrinMessage;
import de.felix_klauke.doctrin.commons.message.DoctrinMessageContext;

/**
 * The interface of the core application. You can have a look at {@link DoctrinCoreApplicationImpl}
 * for the default implementation.
 *
 * The core contains the main functionality and provides API access of the message handling and
 * client management engine.
 *
 * @author Felix Klauke <fklauke@itemis.de>
 */
public interface DoctrinCoreApplication {

    /**
     * Handle an incoming message in the given context.
     *
     * @param messageContext The context of the message.
     * @param message        The message.
     */
    void handleMessage(DoctrinMessageContext messageContext, DoctrinMessage message);
}
