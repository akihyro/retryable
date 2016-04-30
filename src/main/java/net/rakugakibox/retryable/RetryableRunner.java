package net.rakugakibox.retryable;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides the execution of a retryable processing and a retry processing.
 *
 * @param <T> the result type.
 */
@RequiredArgsConstructor(access = AccessLevel.MODULE)
@Slf4j
public class RetryableRunner<T> {

    /**
     * The retryable processing.
     */
    @NonNull
    private final RetryableFunction<T> function;

    /**
     * The retry processing.
     */
    @NonNull
    private final RetryHandler handler;

    /**
     * Runs the retryable processing and the retry processing.
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
                    return function.call(context);
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
