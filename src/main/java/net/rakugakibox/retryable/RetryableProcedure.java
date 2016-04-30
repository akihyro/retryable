package net.rakugakibox.retryable;

/**
 * The procedure that retryable processing.
 * Has a context argument. Does not have a return value.
 */
@FunctionalInterface
public interface RetryableProcedure {

    /**
     * Runs the procedure.
     *
     * @param context the context.
     * @throws Exception if an exception occurs.
     */
    void run(RetryableContext context) throws Exception;

}
