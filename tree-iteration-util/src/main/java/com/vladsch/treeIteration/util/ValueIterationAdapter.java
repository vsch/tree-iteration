package com.vladsch.treeIteration.util;

import org.jetbrains.annotations.NotNull;

public interface ValueIterationAdapter<N, T> {

    @NotNull
    ValueIterationConsumerAdapter<N, T> getConsumerAdapter();

    @NotNull
    <V> ValueIterationAdapter<N, V> andThen(ValueIterationAdapter<? super T, V> after);

    @NotNull
    ValueIterationAdapter<N, T> compose(ValueIterationAdapter<? super N, N> before);
}
