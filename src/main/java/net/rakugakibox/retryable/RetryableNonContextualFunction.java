package net.rakugakibox.retryable;

/**
 * Represents a retryable processing.
 * Does not have a context argument. Has a return value.
 *
 * @param <T> the result type.
 */
@FunctionalInterface
public interface RetryableNonContextualFunction<T> {

    /**
     * Runs the retryable processing.
     *
     * @return the result.
     * @throws Exception if an exception occurs.
     */
    T run() throws Exception;

}
