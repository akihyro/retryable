package net.rakugakibox.retryable;

/**
 * The retryable process.
 *
 * @param <T> the result type.
 */
@FunctionalInterface
public interface RetryableProcess<T> {

    /**
     * Performs the process.
     *
     * @param context the context.
     * @return the result.
     * @throws Exception if an exception occurs.
     */
    T perform(RetryableContext context) throws Exception;

    /**
     * The retryable process.
     * Has a context argument, and a return value.
     *
     * @param <T> the result type.
     */
    @FunctionalInterface
    interface Function<T> extends RetryableProcess<T> {

        /** {@inheritDoc} */
        @Override
        public default T perform(RetryableContext context) throws Exception {
            return call(context);
        }

        /**
         * Calls the process.
         *
         * @param context the context.
         * @return the result.
         * @throws Exception if an exception occurs.
         */
        T call(RetryableContext context) throws Exception;

    }

    /**
     * The retryable process.
     * Does not have a context argument. Has a return value.
     */
    @FunctionalInterface
    interface NonContextualFunction<T> extends RetryableProcess<T> {

        /** {@inheritDoc} */
        @Override
        public default T perform(RetryableContext context) throws Exception {
            return call();
        }

        /**
         * Calls the process.
         *
         * @return the result.
         * @throws Exception if an exception occurs.
         */
        T call() throws Exception;

    }

    /**
     * The retryable process.
     * Has a context argument. Does not have a return value.
     */
    @FunctionalInterface
    interface Procedure extends RetryableProcess<Void> {

        /** {@inheritDoc} */
        @Override
        public default Void perform(RetryableContext context) throws Exception {
            run(context);
            return null;
        }

        /**
         * Runs the process.
         *
         * @param context the context.
         * @throws Exception if an exception occurs.
         */
        void run(RetryableContext context) throws Exception;

    }

    /**
     * The retryable process.
     * Does not have a context argument, and a return value.
     */
    @FunctionalInterface
    interface NonContextualProcedure extends RetryableProcess<Void> {

        /** {@inheritDoc} */
        @Override
        public default Void perform(RetryableContext context) throws Exception {
            run();
            return null;
        }

        /**
         * Runs the process.
         *
         * @throws Exception if an exception occurs.
         */
        void run() throws Exception;

    }

}
