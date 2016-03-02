package net.rakugakibox.retryable;

/**
 * This exception is thrown when the retryable function cannot be retried.
 */
public class CannotRetryException extends RuntimeException {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * the context.
     */
    private final RetryableContext context;

    /**
     * Constructs a new instance.
     *
     * @param message the detail message.
     * @param cause the cause.
     * @param context the context.
     */
    CannotRetryException(String message, Throwable cause, RetryableContext context) {
        super(message, cause);
        this.context = context;
    }

    /**
     * Returns the context.
     *
     * @return the context.
     */
    public RetryableContext context() {
        return context;
    }

}
