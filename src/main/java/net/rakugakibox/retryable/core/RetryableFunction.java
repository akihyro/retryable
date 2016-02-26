package net.rakugakibox.retryable.core;

/**
 * Retryable function.
 *
 * @param <T> result type of the function.
 * @param <E> exception type of the function.
 */
@FunctionalInterface
public interface RetryableFunction<T, E extends Throwable> {

    /**
     * Apply the function.
     *
     * @param context context of the function.
     * @return result of the function.
     * @throws E if an exception occurs in the function.
     */
    T apply(RetryableContext context) throws E;

}
