package net.rakugakibox.retryable;

import lombok.NonNull;

/**
 * This functional interface represents a retry processing.
 */
@FunctionalInterface
public interface RetryHandler {

    /**
     * Handle the retry processing.
     *
     * @param context the context.
     * @throws CannotRetryException if cannot retry.
     */
    void handle(RetryableContext context) throws CannotRetryException;

    /**
     * Compose the handler.
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
     * Return the handler that does nothing.
     *
     * @return the handler that does nothing.
     */
    static RetryHandler nop() {
        return context -> {};
    }

}
