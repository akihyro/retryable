package net.rakugakibox.retryable.core;

/**
 * Retryable function.
 *
 * @param <T> result type of the function.
 */
@FunctionalInterface
public interface RetryableFunction<T> {

    /**
     * Apply the function.
     *
     * @param context context of the function.
     * @return result of the function.
     * @throws Throwable if an exception occurs in the function.
     */
    T apply(RetryableContext context) throws Throwable;

}
