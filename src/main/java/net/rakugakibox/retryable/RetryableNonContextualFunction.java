package net.rakugakibox.retryable;

/**
 * The function that retryable processing.
 * Does not have a context argument. Has a return value.
 *
 * @param <T> the result type.
 */
@FunctionalInterface
public interface RetryableNonContextualFunction<T> {

    /**
     * Calls the function.
     *
     * @return the result.
     * @throws Exception if an exception occurs.
     */
    T call() throws Exception;

}
