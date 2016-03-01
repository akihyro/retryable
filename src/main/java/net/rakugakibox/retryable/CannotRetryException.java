package net.rakugakibox.retryable;

/**
 * This exception is thrown, if can not retry.
 */
public class CannotRetryException extends RuntimeException {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     */
    CannotRetryException() {
    }

    /**
     * Constructor.
     *
     * @param message the detail message.
     */
    CannotRetryException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause the cause.
     */
    CannotRetryException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     *
     * @param message the detail message.
     * @param cause the cause.
     */
    CannotRetryException(String message, Throwable cause) {
        super(message, cause);
    }

}
