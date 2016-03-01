package net.rakugakibox.retryable;

/**
 * This functional interface represents a retryable processing.
 * It has a return value.
 *
 * @param <T> the result type.
 */
@FunctionalInterface
public interface RetryableTypedFunction<T> {

    /**
     * Run the function.
     *
     * @param context the context.
     * @return the result.
     * @throws Exception if an exception occurs.
     */
    T run(RetryableContext context) throws Exception;

}
