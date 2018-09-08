package de.d3adspace.doctrin.commons.kernel;

import de.d3adspace.doctrin.commons.message.DoctrinMessage;
import de.d3adspace.doctrin.commons.message.DoctrinMessageContext;

/**
 * The kernel the central IO processor that will take incoming messages and process them in a certain context.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
public interface Kernel {

    /**
     * Process the given message in the given context.
     *
     * @param messageContext The message context.
     * @param message        The message.
     */
    void processMessage(DoctrinMessageContext messageContext, DoctrinMessage message);
}
