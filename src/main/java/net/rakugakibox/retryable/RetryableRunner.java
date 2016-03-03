package net.rakugakibox.retryable;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Provides the execution process of a retryable function and a retry handler.
 *
 * @param <T> the result type.
 */
@RequiredArgsConstructor(access = AccessLevel.MODULE)
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
        while (true) {
            try {
                return function.run(context.next());
            } catch (Exception exc) {
                handler.handle(context.fail(exc));
            }
        }
    }

}
