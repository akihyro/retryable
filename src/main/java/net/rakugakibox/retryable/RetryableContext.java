package net.rakugakibox.retryable;

import java.util.ArrayList;
import static java.util.Collections.unmodifiableList;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * This class has the running information.
 */
@RequiredArgsConstructor(access = AccessLevel.MODULE)
public class RetryableContext {

    /**
     * Number of times.
     */
    long times = 0;

    /**
     * Exceptions that occurred.
     */
    final List<Exception> exceptions = new ArrayList<>();

    /**
     * Return the number of times.
     *
     * @return the number of times.
     */
    public long times() {
        return times;
    }

    /**
     * Return the exceptions that occurred.
     *
     * @return the exceptions that occurred.
     */
    public List<Exception> exceptions() {
        return unmodifiableList(exceptions);
    }

    /**
     * Return the last exception that occurred.
     *
     * @return the last exception that occurred.
     */
    public Optional<Exception> exception() {
        return exceptions.isEmpty()
                ? Optional.empty()
                : Optional.of(exceptions.get(exceptions.size() - 1));
    }

}
