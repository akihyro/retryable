package net.rakugakibox.retryable;

import java.time.Duration;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.Collection;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;
import lombok.NonNull;

/**
 * The main class of "Retryable".
 */
public class Retryable {

    /**
     * Number of tries.
     */
    long tries = 2;

    /**
     * Retrying exception classes.
     */
    Collection<Class<? extends Exception>> exceptions = singletonList(Exception.class);

    /**
     * Retrying interval.
     */
    Duration interval = Duration.ZERO;

    /**
     * Set number of tries.
     * Should an exception occur, it'll retry for {@code (tries - 1)} times.
     *
     * @param tries number of tries.
     * @return this instance.
     */
    public Retryable tries(long tries) {
        this.tries = tries;
        return this;
    }

    /**
     * Set retrying exception classes.
     *
     * @param exceptions retrying exception classes.
     * @return this instance.
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public final Retryable on(@NonNull Class<? extends Exception>... exceptions) {
        return on(asList(exceptions));
    }

    /**
     * Set retrying exception classes.
     *
     * @param exceptions retrying exception classes.
     * @return this instance.
     */
    public Retryable on(@NonNull Collection<Class<? extends Exception>> exceptions) {
        this.exceptions = unmodifiableList(new ArrayList<>(exceptions));
        return this;
    }

    /**
     * Set retrying interval.
     *
     * @param interval retrying interval.
     * @return this instance.
     */
    public Retryable interval(@NonNull Duration interval) {
        this.interval = interval;
        return this;
    }

    /**
     * Run the specified function.
     * If an particular exception occurs, retries.
     *
     * @param <T> result type of the function.
     * @param function retryable function.
     * @return result of the function.
     */
    public <T> T run(@NonNull RetryableTypedFunction<T> function) {
        RetryableContext context = new RetryableContext();
        while (context.times < tries) {
            try {
                context.times++;
                return function.run(context);
            } catch (Exception exc) {
                context.exceptions.add(exc);
                if (!exceptions.stream().anyMatch(c -> c.isInstance(exc))) {
                    throw new RuntimeException(exc);  // TODO: Add original exception class.
                }
            }
            // TODO: Thread.sleep(interval.toMillis(), interval.getNano());
        }
        throw new RuntimeException();  // TODO: Add original exception class.
    }

}
