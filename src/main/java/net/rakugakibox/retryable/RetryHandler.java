package net.rakugakibox.retryable;

import lombok.NonNull;

/**
 * The handler that performs retry processing.
 */
@FunctionalInterface
public interface RetryHandler {

    /**
     * Handles the retry processing.
     *
     * @param context the context.
     * @throws CannotRetryException if cannot retry.
     */
    void handle(RetryableContext context) throws CannotRetryException;

    /**
     * Composes the handler.
     *
     * @param after the subsequent handler.
     * @return the composed handler.
     */
    default RetryHandler andThen(@NonNull RetryHandler after) {
        return context -> {
            handle(context);
            after.handle(context);
        };
    }

    /**
     * Returns a handler that does nothing.
     *
     * @return a handler that does nothing.
     */
    static RetryHandler nop() {
        return context -> {};
    }

}
