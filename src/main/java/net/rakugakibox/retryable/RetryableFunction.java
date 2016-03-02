package net.rakugakibox.retryable;

/**
 * Represents a retryable processing.
 * Has a context argument, and a return value.
 *
 * @param <T> the result type.
 */
@FunctionalInterface
public interface RetryableFunction<T> {

    /**
     * Runs the retryable processing.
     *
     * @param context the context.
     * @return the result.
     * @throws Exception if an exception occurs.
     */
    T run(RetryableContext context) throws Exception;

}
