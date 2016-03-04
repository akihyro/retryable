package net.rakugakibox.retryable;

import static java.lang.Thread.sleep;
import java.time.Duration;
import static java.util.Arrays.asList;
import java.util.Collection;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides a retry of the code block.
 */
@Slf4j
public class Retryable {

    /**
     * The retry handler.
     */
    private RetryHandler handler = RetryHandler.nop();

    /**
     * Adds the retry processing.
     *
     * @param handler the retry handler.
     * @return this instance.
     */
    public Retryable on(@NonNull RetryHandler handler) {
        log.debug("Adds the retry processing: {}", handler);
        this.handler = this.handler.andThen(handler);
        return this;
    }

    /**
     * Limits the retryable exception types.
     *
     * @param types the retryable exception types.
     * @return this instance.
     */
    public Retryable on(@NonNull Collection<Class<? extends Exception>> types) {
        log.debug("Limits the retryable exception types: {}", types);
        return on(context -> {
            Exception exception = context.exception().get();
            log.debug("Checks the limit of the retryable exception types: exception={}, types={}", exception, types);
            if (types.stream().anyMatch(type -> type.isInstance(exception))) {
                log.debug("An exception type did not match: exception={}, types={}", exception, types);
                throw new CannotRetryException("An exception type did not match", exception, context);
            }
        });
    }

    /**
     * Limits the retryable exception types.
     *
     * @param types the retryable exception types.
     * @return this instance.
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public final Retryable on(@NonNull Class<? extends Exception>... types) {
        return on(asList(types));
    }

    /**
     * Limits the number of retries.
     * Should an exception occur, it'll retry for {@code (retries)} times.
     *
     * @param retries the maximum number of retries.
     * @return this instance.
     */
    public Retryable retries(int retries) {
        log.debug("Limits the number of retries: {}", retries);
        if (retries < 0) {
            throw new IllegalArgumentException("retries < 0");
        }
        return on(context -> {
            int times = context.times();
            log.debug("Checks the limit of the maximum number of retries: times={}, retries={}", times, retries);
            if (times > retries) {
                log.debug("Maximum number of retry attempts reached: times={}, retries={}", times, retries);
                throw new CannotRetryException(
                        "Maximum number of retry attempts reached", context.exception().get(), context);
            }
        });
    }

    /**
     * Limits the number of tries.
     * Should an exception occur, it'll retry for {@code (tries - 1)} times.
     *
     * @param tries the maximum number of tries.
     * @return this instance.
     */
    public Retryable tries(int tries) {
        return retries(tries - 1);
    }

    /**
     * Adds the interval.
     *
     * @param interval the duration.
     * @return this instance.
     */
    public Retryable interval(@NonNull Duration duration) {
        log.debug("Adds the interval: {}", duration);
        return on(context -> {
            try {
                log.debug("Sleeps: duration={}", duration);
                sleep(duration.toMillis(), duration.getNano());
            } catch (InterruptedException exception) {
                log.debug("A sleep was interrupted: exception={}, duration={}", exception, duration);
                throw new CannotRetryException("A sleep was interrupted", exception, context);
            }
        });
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
