package net.rakugakibox.retryable;

/**
 * Represents a retryable processing.
 * Does not have a context argument, and a return value.
 */
@FunctionalInterface
public interface RetryableNonContextualProcedure {

    /**
     * Runs the retryable processing.
     *
     * @throws Exception if an exception occurs.
     */
    void run() throws Exception;

}
