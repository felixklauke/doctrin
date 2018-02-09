package de.felix_klauke.doctrin.core;

import de.felix_klauke.doctrin.commons.message.ActionCode;
import de.felix_klauke.doctrin.commons.message.DoctrinMessage;
import de.felix_klauke.doctrin.commons.message.DoctrinMessageContext;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinCoreApplicationImpl implements DoctrinCoreApplication {

    @Override
    public void handleMessage(DoctrinMessageContext messageContext, DoctrinMessage message) {
        ActionCode actionCode = message.getActionCode();

        switch (actionCode) {
            case UPDATE_SUBSCRIBER_NAME: {
                handleMessageUpdateSubscriberName(messageContext, message);
                break;
            }
        }
    }

    private void handleMessageUpdateSubscriberName(DoctrinMessageContext messageContext, DoctrinMessage message) {

    }
}
