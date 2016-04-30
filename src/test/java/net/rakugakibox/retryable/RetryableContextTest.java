package net.rakugakibox.retryable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.Test;

/**
 * The test of {@link RetryableContext}.
 */
public class RetryableContextTest {

    /**
     * Tests for {@link RetryableContext#RetryableContext()}.
     */
    @Test
    public void new_() {
        RetryableContext context = new RetryableContext();
        assertThat(context.times())
                .isEqualTo(0L);
        assertThat(context.exceptions())
                .isEmpty();
    }

    /**
     * Tests for {@link RetryableContext#next()}.
     */
    @Test
    public void next_() {
        RetryableContext context = new RetryableContext();
        assertThat(context.next())
                .isEqualTo(context);
        assertThat(context.times())
                .isEqualTo(1L);
        assertThat(context.exceptions())
                .isEmpty();
    }

    /**
     * Tests for {@link RetryableContext#next()}.
     */
    @Test
    public void next_withoutFail() {
        RetryableContext context = new RetryableContext();
        context.next();
        assertThatThrownBy(() -> context.next())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("The exception has not been stacked.");
    }

    /**
     * Tests for {@link RetryableContext#fail(Exception)}.
     */
    @Test
    public void fail_() {
        RetryableContext context = new RetryableContext();
        Exception cause = new Exception("the cause message.");
        context.next();
        assertThat(context.fail(cause))
                .isEqualTo(context);
        assertThat(context.times())
                .isEqualTo(1L);
        assertThat(context.exceptions())
                .containsExactly(cause);
    }

    /**
     * Tests for {@link RetryableContext#fail(Exception)}.
     */
    @Test
    public void fail_multiFailed() {
        RetryableContext context = new RetryableContext();
        Exception cause1 = new Exception("the cause1 message.");
        Exception cause2 = new Exception("the cause2 message.");
        context.next().fail(cause1).next().fail(cause2);
        assertThat(context.times())
                .isEqualTo(2L);
        assertThat(context.exceptions())
                .containsExactly(cause1, cause2);
    }

    /**
     * Tests for {@link RetryableContext#fail(Exception)}.
     */
    @Test
    public void fail_passNullToCause() {
        RetryableContext context = new RetryableContext();
        assertThatThrownBy(() -> context.fail(null))
                .isInstanceOf(NullPointerException.class);
    }

    /**
     * Tests for {@link RetryableContext#fail(Exception)}.
     */
    @Test
    public void fail_withoutNext() {
        RetryableContext context = new RetryableContext();
        Exception cause = new Exception("the cause message.");
        assertThatThrownBy(() -> context.fail(cause))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("The time is not advanced.");
    }

    /**
     * Tests for {@link RetryableContext#exception()}.
     */
    @Test
    public void exception_() {
        RetryableContext context = new RetryableContext();
        Exception cause = new Exception("the cause message.");
        context.next().fail(cause);
        assertThat(context.exception())
                .contains(cause);
    }

    /**
     * Tests for {@link RetryableContext#exception()}.
     */
    @Test
    public void exception_multiFailed() {
        RetryableContext context = new RetryableContext();
        Exception cause1 = new Exception("the cause1 message.");
        Exception cause2 = new Exception("the cause2 message.");
        context.next().fail(cause1).next().fail(cause2);
        assertThat(context.exception())
                .contains(cause2);
    }

    /**
     * Tests for {@link RetryableContext#exception()}.
     */
    @Test
    public void exception_firstTime() {
        RetryableContext context = new RetryableContext();
        assertThat(context.exception())
                .isEmpty();
    }

    /**
     * Tests for {@link RetryableContext#toString()}.
     */
    @Test
    public void toString_() {
        RetryableContext context = new RetryableContext();
        assertThat(context)
                .hasToString("RetryableContext(0 times, 0 exceptions)");
    }

    /**
     * Tests for {@link RetryableContext#toString()}.
     */
    @Test
    public void toString_withNext() {
        RetryableContext context = new RetryableContext();
        context.next();
        assertThat(context)
                .hasToString("RetryableContext(1 times, 0 exceptions)");
    }

    /**
     * Tests for {@link RetryableContext#toString()}.
     */
    @Test
    public void toString_withNextAndFail() {
        RetryableContext context = new RetryableContext();
        context.next().fail(new Exception("the cause message."));
        assertThat(context)
                .hasToString("RetryableContext(1 times, 1 exceptions)");
    }

}
