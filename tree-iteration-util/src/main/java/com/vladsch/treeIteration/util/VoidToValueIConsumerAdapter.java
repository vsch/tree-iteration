package com.vladsch.treeIteration.util;

import org.jetbrains.annotations.NotNull;

public class VoidToValueIConsumerAdapter<N, R> implements ValueIterationConsumer<N, R> {
    final private @NotNull VoidIterationConsumer<N> myConsumer;

    public VoidToValueIConsumerAdapter(@NotNull final VoidIterationConsumer<N> consumer) {
        myConsumer = consumer;
    }

    @Override
    public void accept(@NotNull final N it, @NotNull final ValueIteration<R> iteration) {
        myConsumer.accept(it, iteration);
    }

    @Override
    public void beforeStart(@NotNull final ValueIteration<R> iteration) {
        myConsumer.beforeStart(iteration);

    }

    @Override
    public void startRecursion(@NotNull final VoidIteration iteration) {
        myConsumer.startRecursion(iteration);
    }

    @Override
    public void endRecursion(@NotNull final VoidIteration iteration) {
        myConsumer.endRecursion(iteration);

    }

    @Override
    public void afterEnd(@NotNull final ValueIteration<R> iteration) {
        myConsumer.afterEnd(iteration);
    }
}
