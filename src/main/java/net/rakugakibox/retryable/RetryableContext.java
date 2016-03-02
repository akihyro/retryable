package net.rakugakibox.retryable;

import java.util.ArrayList;
import static java.util.Collections.unmodifiableList;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Has the running information.
 */
@RequiredArgsConstructor(access = AccessLevel.MODULE)
public class RetryableContext {

    /**
     * The number of times.
     */
    long times = 0;

    /**
     * The exceptions that occurred.
     */
    final List<Exception> exceptions = new ArrayList<>();

    /**
     * Returns the number of times.
     *
     * @return the number of times.
     */
    public long times() {
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

}
