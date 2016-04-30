package net.rakugakibox.retryable;

import lombok.NonNull;

/**
 * The retry handler.
 */
@FunctionalInterface
public interface RetryHandler {

    /**
     * Handles the handler.
     *
     * @param context the context.
     * @throws CannotRetryException if cannot retry.
     */
    void handle(RetryableContext context) throws CannotRetryException;

    /**
     * Composes the handler.
     *
     * @param handler the subsequent handler.
     * @return the composed handler.
     */
    default RetryHandler andThen(@NonNull RetryHandler handler) {
        return context -> {
            handle(context);
            handler.handle(context);
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
