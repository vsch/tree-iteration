package com.vladsch.tree.iteration;

import org.jetbrains.annotations.NotNull;

public interface IterationConsumer<N> {
    // starting a new recursion iteration
    //void startRecursion(@NotNull VoidIteration iteration);
    default void startRecursion(@NotNull VoidIteration iteration) {

    }

    // after recursion is done but before stack is adjusted for new level
    //void endRecursion(@NotNull VoidIteration iteration);
    default void endRecursion(@NotNull VoidIteration iteration) {

    }
}
