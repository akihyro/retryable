package net.rakugakibox.retryable;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

/**
 * The test of {@link CannotRetryException}.
 */
public class CannotRetryExceptionTest {

    /**
     * Tests for {@link CannotRetryException#CannotRetryException(String, Throwable, RetryableContext)}.
     */
    @Test
    public void construct() {
        String message = "the exception message";
        Exception cause = new Exception("the cause message");
        RetryableContext context = new RetryableContext();
        CannotRetryException exception = new CannotRetryException(message, cause, context);
        assertThat(exception)
                .hasMessage(message)
                .hasCause(cause);
        assertThat(exception.context())
                .isEqualTo(context);
    }

}
