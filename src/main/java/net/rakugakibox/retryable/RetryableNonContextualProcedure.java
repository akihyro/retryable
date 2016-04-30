package net.rakugakibox.retryable;

/**
 * The procedure that retryable processing.
 * Does not have a context argument, and a return value.
 */
@FunctionalInterface
public interface RetryableNonContextualProcedure {

    /**
     * Runs the procedure.
     *
     * @throws Exception if an exception occurs.
     */
    void run() throws Exception;

}
