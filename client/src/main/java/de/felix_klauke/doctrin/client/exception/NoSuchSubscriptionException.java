package de.felix_klauke.doctrin.client.exception;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class NoSuchSubscriptionException extends RuntimeException {

    public NoSuchSubscriptionException(String message) {
        super(message);
    }
}
