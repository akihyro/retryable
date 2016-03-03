package net.rakugakibox.retryable;

import static java.lang.Thread.sleep;
import java.time.Duration;
import static java.util.Arrays.asList;
import java.util.Collection;
import lombok.NonNull;

/**
 * Provides a retry of the code block.
 */
public class Retryable {

    /**
     * The retry handler.
     */
    private RetryHandler handler = RetryHandler.nop();

    /**
     * Limits the number of retries.
     * Should an exception occur, it'll retry for {@code (retries)} times.
     *
     * @param retries the maximum number of retries.
     * @return the this instance.
     */
    public Retryable retries(int retries) {
        if (retries < 0) {
            throw new IllegalArgumentException("retries < 0");
        }
        handler = handler.andThen(context -> {
            if (context.times() <= retries) {
                Exception exc = context.exception().get();
                throw new CannotRetryException("Maximum number of retry attempts reached", exc, context);
            }
        });
        return this;
    }

    /**
     * Limits the number of tries.
     * Should an exception occur, it'll retry for {@code (tries - 1)} times.
     *
     * @param tries the maximum number of tries.
     * @return the this instance.
     */
    public Retryable tries(int tries) {
        return retries(tries - 1);
    }

    /**
     * Limits the retryable exception types.
     *
     * @param types the retryable exception types.
     * @return the this instance.
     */
    public Retryable on(@NonNull Collection<Class<? extends Exception>> types) {
        handler = handler.andThen(context -> {
            Exception exc = context.exception().get();
            if (types.stream().anyMatch(type -> type.isInstance(exc))) {
                throw new CannotRetryException("An exception type did not match", exc, context);
            }
        });
        return this;
    }

    /**
     * Limits the retryable exception types.
     *
     * @param types the retryable exception types.
     * @return the this instance.
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public final Retryable on(@NonNull Class<? extends Exception>... types) {
        return on(asList(types));
    }

    /**
     * Adds the interval.
     *
     * @param interval the interval.
     * @return the this instance.
     */
    public Retryable interval(@NonNull Duration interval) {
        handler = handler.andThen(context -> {
            try {
                sleep(interval.toMillis(), interval.getNano());
            } catch (InterruptedException exc) {
                throw new CannotRetryException(exc.getMessage(), exc, context);
            }
        });
        return this;
    }

    /**
     * Returns the runner.
     *
     * @param <T> the result type.
     * @param function the retryable function.
     * @return the runner.
     */
    public <T> RetryableRunner<T> wrap(RetryableFunction<T> function) {
        return new RetryableRunner<>(function, handler);
    }

    /**
     * Returns the runner.
     * The runner always returns null.
     *
     * @param function the retryable function.
     * @return the runner.
     */
    public RetryableRunner<?> wrap(RetryableVoidFunction function) {
        return wrap(context -> {
            function.run(context);
            return null;
        });
    }

}
