package de.felix_klauke.doctrin.client.exception;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class NoSuchSubscriptionException extends RuntimeException {

    public NoSuchSubscriptionException() {
    }

    public NoSuchSubscriptionException(String message) {
        super(message);
    }

    public NoSuchSubscriptionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchSubscriptionException(Throwable cause) {
        super(cause);
    }

    public NoSuchSubscriptionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
