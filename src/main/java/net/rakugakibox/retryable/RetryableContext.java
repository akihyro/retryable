package net.rakugakibox.retryable;

import java.util.ArrayList;
import static java.util.Collections.unmodifiableList;
import java.util.List;

/**
 * Retryable context.
 */
public class RetryableContext {

    /**
     * Number of times.
     */
    long times = 0;

    /**
     * Exceptions that occurred in function.
     */
    List<Exception> exceptions = new ArrayList<>();

    /**
     * Constructor.
     */
    RetryableContext() {
    }

    /**
     * Return number of times.
     *
     * @return number of times.
     */
    public long times() {
        return times;
    }

    /**
     * Return exceptions that occurred in function.
     *
     * @return exceptions that occurred in function.
     */
    public List<Exception> exceptions() {
        return unmodifiableList(exceptions);
    }

}
