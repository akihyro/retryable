package net.rakugakibox.retryable;

import static java.lang.Thread.sleep;
import java.time.Duration;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.Collection;
import static java.util.Collections.unmodifiableList;
import java.util.concurrent.TimeUnit;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides a retry of the code block.
 * Runs a code block, and retries it when an exception occurs.
 */
@Slf4j
public class Retryable {

    /**
     * The retry handler.
     */
    private RetryHandler handler = RetryHandler.nop();

    /**
     * Constructs an instance.
     */
    public Retryable() {
    }

    /**
     * Adds the retry processing.
     *
     * @param handler the retry processing.
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
        Collection<Class<? extends Exception>> unmodifiableTypes = unmodifiableList(new ArrayList<>(types));
        log.debug("Limits the retryable exception types: {}", unmodifiableTypes);
        return on(context -> {
            Exception exception = context.exception().get();
            log.debug("Checks the limit of the retryable exception types: "
                    + "exception={}, types={}", exception, unmodifiableTypes);
            if (!unmodifiableTypes.stream().anyMatch(type -> type.isInstance(exception))) {
                log.debug("An exception type did not match: exception={}, types={}", exception, unmodifiableTypes);
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
    public Retryable retries(long retries) {
        log.debug("Limits the number of retries: {}", retries);
        return on(context -> {
            long times = context.times();
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
    public Retryable tries(long tries) {
        return retries(tries - 1L);
    }

    /**
     * Adds the interval.
     *
     * @param duration the duration.
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
     * Adds the interval.
     *
     * @param duration the duration.
     * @param unit the unit of duration.
     * @return this instance.
     */
    public Retryable interval(long duration, TimeUnit unit) {
        return interval(Duration.ofNanos(unit.toNanos(duration)));
    }

    /**
     * Adds the interval.
     *
     * @param millis the duration in milliseconds.
     * @param nanos the duration in nanoseconds.
     * @return this instance.
     */
    public Retryable interval(long millis, int nanos) {
        return interval(Duration.ofMillis(millis).withNanos(nanos));
    }

    /**
     * Adds the interval.
     *
     * @param millis the duration in milliseconds.
     * @return this instance.
     */
    public Retryable interval(long millis) {
        return interval(Duration.ofMillis(millis));
    }

    /**
     * Returns a processor.
     *
     * @param <T> the result type.
     * @param process the retryable process.
     * @return a processor.
     */
    public <T> RetryableProcessor<T> process(RetryableProcess<T> process) {
        return new RetryableProcessor<>(process, handler);
    }

    /**
     * Returns a processor.
     *
     * @param <T> the result type.
     * @param function the retryable process.
     * @return a processor.
     */
    public <T> RetryableProcessor<T> function(RetryableProcess.Function<T> function) {
        return process(function);
    }

    /**
     * Returns a processor.
     *
     * @param <T> the result type.
     * @param function the retryable process.
     * @return a processor.
     */
    public <T> RetryableProcessor<T> function(RetryableProcess.NonContextualFunction<T> function) {
        return process(function);
    }

    /**
     * Returns a processor.
     *
     * @param procedure the retryable process.
     * @return a processor.
     */
    public RetryableProcessor<Void> procedure(RetryableProcess.Procedure procedure) {
        return process(procedure);
    }

    /**
     * Returns a processor.
     *
     * @param procedure the retryable process.
     * @return a processor.
     */
    public RetryableProcessor<Void> procedure(RetryableProcess.NonContextualProcedure procedure) {
        return process(procedure);
    }

    /**
     * Performs the retryable process, and handle the retry handler.
     *
     * @param <T> the result type.
     * @param process the retryable process.
     * @return the result.
     * @throws CannotRetryException if cannot retry.
     */
    public <T> T perform(RetryableProcess<T> process) throws CannotRetryException {
        return process(process).perform();
    }

    /**
     * Calls the retryable process, and handle the retry handler.
     *
     * @param <T> the result type.
     * @param function the retryable process.
     * @return the result.
     * @throws CannotRetryException if cannot retry.
     */
    public <T> T call(RetryableProcess.Function<T> function) throws CannotRetryException {
        return function(function).perform();
    }

    /**
     * Calls the retryable process, and handle the retry handler.
     *
     * @param <T> the result type.
     * @param function the retryable process.
     * @return the result.
     * @throws CannotRetryException if cannot retry.
     */
    public <T> T call(RetryableProcess.NonContextualFunction<T> function) throws CannotRetryException {
        return function(function).perform();
    }

    /**
     * Runs the retryable process, and handle the retry handler.
     *
     * @param procedure the retryable process.
     * @throws CannotRetryException if cannot retry.
     */
    public void run(RetryableProcess.Procedure procedure) throws CannotRetryException {
        procedure(procedure).perform();
    }

    /**
     * Runs the retryable process, and handle the retry handler.
     *
     * @param procedure the retryable process.
     * @throws CannotRetryException if cannot retry.
     */
    public void run(RetryableProcess.NonContextualProcedure procedure) throws CannotRetryException {
        procedure(procedure).perform();
    }

}
