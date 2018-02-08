package de.felix_klauke.doctrin.commons.message;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class DoctrinMessageWrapper {

    private final DoctrinMessage message;
    private final DoctrinMessageContext messageContext;

    public DoctrinMessageWrapper(DoctrinMessage message, DoctrinMessageContext messageContext) {
        this.message = message;
        this.messageContext = messageContext;
    }

    public DoctrinMessage getMessage() {
        return message;
    }

    public DoctrinMessageContext getMessageContext() {
        return messageContext;
    }
}
