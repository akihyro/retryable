package net.rakugakibox.retryable;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides the execution process of a retryable function and a retry handler.
 *
 * @param <T> the result type.
 */
@RequiredArgsConstructor(access = AccessLevel.MODULE)
@Slf4j
public class RetryableRunner<T> {

    /**
     * The retryable function.
     */
    private final RetryableFunction<T> function;

    /**
     * The retry handler.
     */
    private final RetryHandler handler;

    /**
     * Runs the retryable function and the retry handler.
     *
     * @return the result.
     * @throws CannotRetryException if cannot retry.
     */
    public T run() throws CannotRetryException {
        RetryableContext context = new RetryableContext();
        try {
            while (true) {
                try {
                    context.next();
                    log.debug("Runs the function: function={}, context={}", function, context);
                    return function.run(context);
                } catch (Exception exception) {
                    context.fail(exception);
                    log.debug("Function failed. Runs the retry handleer: "
                            + "exception={}, handler={}, context={}", exception, handler, context);
                    handler.handle(context);
                }
            }
        } catch (CannotRetryException exception) {
            log.debug("CannotRetryException occurred: exception={}, context={}", exception, context);
            throw exception;
        }
    }

}
