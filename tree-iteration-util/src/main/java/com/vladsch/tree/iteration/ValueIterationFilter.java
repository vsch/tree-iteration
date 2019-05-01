package com.vladsch.tree.iteration;

public interface ValueIterationFilter<N> {
    boolean filter(N it, VoidIteration loop);
}
