package net.rakugakibox.retryable;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * The processor of {@link RetryableProcess}.
 *
 * @param <T> the result type.
 */
@Slf4j
public class RetryableProcessor<T> {

    /**
     * The retryable process.
     */
    private final RetryableProcess<T> process;

    /**
     * The retry handler.
     */
    private final RetryHandler handler;

    /**
     * Constructs an instance.
     *
     * @param process the retryable process.
     * @param handler the retry handler.
     */
    RetryableProcessor(@NonNull RetryableProcess<T> process, @NonNull RetryHandler handler) {
        this.process = process;
        this.handler = handler;
    }

    /**
     * Performs the retryable process, and handle the retry handler.
     *
     * @return the result.
     * @throws CannotRetryException if cannot retry.
     */
    public T perform() throws CannotRetryException {
        RetryableContext context = new RetryableContext();
        try {
            while (true) {
                try {
                    context.next();
                    log.debug("Performs the process: process={}, context={}", process, context);
                    return process.perform(context);
                } catch (Exception exc) {
                    context.fail(exc);
                    log.debug("Process failed. Handles the handler: "
                            + "exception={}, handler={}, context={}", exc, handler, context);
                    handler.handle(context);
                }
            }
        } catch (CannotRetryException exc) {
            log.debug("CannotRetryException occurred: exception={}, context={}", exc, context);
            throw exc;
        }
    }

}
