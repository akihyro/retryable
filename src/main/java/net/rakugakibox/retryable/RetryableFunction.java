package net.rakugakibox.retryable;

/**
 * The function that retryable processing.
 * Has a context argument, and a return value.
 *
 * @param <T> the result type.
 */
@FunctionalInterface
public interface RetryableFunction<T> {

    /**
     * Calls the function.
     *
     * @param context the context.
     * @return the result.
     * @throws Exception if an exception occurs.
     */
    T call(RetryableContext context) throws Exception;

}
