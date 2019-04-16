package com.vladsch.treeIteration.util;

public interface ValueIterationFilter<N> {
    boolean filter(N it, VoidIteration loop);
}
