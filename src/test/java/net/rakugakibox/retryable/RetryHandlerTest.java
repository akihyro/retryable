package net.rakugakibox.retryable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.Test;

/**
 * The test of {@link RetryHandler}.
 */
public class RetryHandlerTest {

    /**
     * Tests for {@link RetryHandler#andThen(RetryHandler)}.
     */
    @Test
    public void andThen_() {
        StringBuilder string = new StringBuilder();
        RetryHandler handler1 = context -> string.append("first: ").append(context.times()).append(" times; ");
        RetryHandler handler2 = context -> string.append("second: ").append(context.times()).append(" times; ");
        handler1.andThen(handler2).handle(new RetryableContext().next());
        assertThat(string)
                .hasToString("first: 1 times; second: 1 times; ");
    }

    /**
     * Tests for {@link RetryHandler#andThen(RetryHandler)}.
     */
    @Test
    public void andThen_passNullToHandler() {
        RetryHandler handler = RetryHandler.nop();
        assertThatThrownBy(() -> handler.andThen(null))
                .isInstanceOf(NullPointerException.class);
    }

    /**
     * Tests for {@link RetryHandler#nop()}.
     */
    @Test
    public void nop_() {
        RetryHandler.nop().handle(null);
    }

}
