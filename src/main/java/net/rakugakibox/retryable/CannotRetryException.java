package net.rakugakibox.retryable;

/**
 * The exception that indicates could not be retried.
 */
public class CannotRetryException extends RuntimeException {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The context.
     */
    private final RetryableContext context;

    /**
     * Constructs an instance.
     *
     * @param message the message.
     * @param cause the cause.
     * @param context the context.
     */
    public CannotRetryException(String message, Throwable cause, RetryableContext context) {
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
