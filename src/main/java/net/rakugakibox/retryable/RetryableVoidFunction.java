package net.rakugakibox.retryable;

/**
 * This functional interface represents a retryable processing.
 * It does not have a return value.
 */
@FunctionalInterface
public interface RetryableVoidFunction {

    /**
     * Run the function.
     *
     * @param context the context.
     * @throws Exception if an exception occurs.
     */
    void run(RetryableContext context) throws Exception;

}
