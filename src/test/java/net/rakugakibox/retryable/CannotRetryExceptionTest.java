package net.rakugakibox.retryable;

import static org.assertj.core.api.Java6Assertions.assertThat;
import org.junit.Test;

/**
 * Test of {@link CannotRetryException}.
 */
public class CannotRetryExceptionTest {

    /**
     * Test of {@link CannotRetryException#CannotRetryException()}.
     */
    @Test
    public void new_default() {
        assertThat(new CannotRetryException())
                .hasMessage(null)
                .hasNoCause();
    }

    /**
     * Test of {@link CannotRetryException#CannotRetryException(String)}.
     */
    @Test
    public void new_withMessage() {
        assertThat(new CannotRetryException("test message"))
                .hasMessage("test message")
                .hasNoCause();
    }

    /**
     * Test of {@link CannotRetryException#CannotRetryException(Throwable)}.
     */
    @Test
    public void new_withCause() {
        Throwable cause = new Exception("test cause message");
        assertThat(new CannotRetryException(cause))
                .hasMessage("java.lang.Exception: test cause message")
                .hasCause(cause);
    }

    /**
     * Test of {@link CannotRetryException#CannotRetryException(String, Throwable)}.
     */
    @Test
    public void new_withMessageAndCause() {
        Throwable cause = new Exception("test cause message");
        assertThat(new CannotRetryException("test message", cause))
                .hasMessage("test message")
                .hasCause(cause);
    }

}
