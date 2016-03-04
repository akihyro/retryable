package net.rakugakibox.retryable;

import java.util.ArrayList;
import static java.util.Collections.unmodifiableList;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Has the running information.
 */
@RequiredArgsConstructor(access = AccessLevel.MODULE)
public class RetryableContext {

    /**
     * The number of times.
     */
    private int times = 0;

    /**
     * The exceptions that occurred.
     */
    private final List<Exception> exceptions = new ArrayList<>();

    /**
     * Advances the times.
     *
     * @return this instance.
     */
    RetryableContext next() {
        times++;
        return this;
    }

    /**
     * Fails the times.
     *
     * @param cause the cause.
     * @return this instance.
     */
    RetryableContext fail(@NonNull Exception cause) {
        exceptions.add(cause);
        return this;
    }

    /**
     * Returns the number of times.
     *
     * @return the number of times.
     */
    public int times() {
        return times;
    }

    /**
     * Returns the exceptions that occurred.
     *
     * @return the exceptions that occurred.
     */
    public List<Exception> exceptions() {
        return unmodifiableList(exceptions);
    }

    /**
     * Returns the last exception that occurred.
     * The first time, returns an empty.
     *
     * @return the last exception that occurred.
     */
    public Optional<Exception> exception() {
        return exceptions.isEmpty()
                ? Optional.empty()
                : Optional.of(exceptions.get(exceptions.size() - 1));
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return RetryableContext.class.getSimpleName()
                + "("
                + times
                + " times, "
                + exceptions.size()
                + " exceptions"
                + ")";
    }

}
