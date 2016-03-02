package net.rakugakibox.retryable;

import static org.assertj.core.api.Java6Assertions.assertThat;
import org.junit.Test;

/**
 * Tests for {@link CannotRetryException}.
 */
public class CannotRetryExceptionTest {

    /**
     * Tests for {@link CannotRetryException#CannotRetryException(String, Throwable, RetryableContext)}.
     */
    @Test
    public void construct() {
        Exception cause = new Exception("cause message");
        RetryableContext context = new RetryableContext();
        CannotRetryException exception = new CannotRetryException("exception message", cause, context);
        assertThat(exception)
                .hasMessage("exception message")
                .hasCause(cause);
        assertThat(exception.context())
                .isEqualTo(context);
    }

}
