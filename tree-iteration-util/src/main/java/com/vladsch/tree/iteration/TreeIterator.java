package com.vladsch.tree.iteration;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Predicate;

public class TreeIterator<N> {
    public final static Logger LOG = LoggerFactory.getILoggerFactory().getLogger("com.vladsch.treeIteration.util.looping");
    public final static Logger LOG_INFO = LoggerFactory.getILoggerFactory().getLogger("com.vladsch.treeIteration.util.looping-summary");
    public final static Logger LOG_TRACE = LoggerFactory.getILoggerFactory().getLogger("com.vladsch.treeIteration.util.looping-detailed");

    public static final Predicate TRUE = o -> true;
    public static final Predicate FALSE = o -> false;
    public static final Predicate NOT_NULL = Objects::nonNull;

    private final IterationConditions<N> myConstraints;
    private final Predicate<? super N> myRecursion;
    protected final Predicate<? super N> myFilter;

    public TreeIterator(final IterationConditions<N> constraints, final Predicate<? super N> filter) {
        //noinspection unchecked
        this(constraints, filter, FALSE);
    }

    public TreeIterator(final IterationConditions<N> constraints) {
        //noinspection unchecked
        this(constraints, TRUE, FALSE);
    }

    public TreeIterator(
            final IterationConditions<N> constraints,
            final Predicate<? super N> filter,
            Predicate<? super N> recursion
    ) {
        myConstraints = constraints;
        myRecursion = recursion;
        myFilter = filter;
    }

    @NotNull
    public Predicate<N> getPredicate(@NotNull final Class clazz) {
        return clazz::isInstance;
    }

    @NotNull
    public <F> Predicate<N> getPredicate(@NotNull final Class<F> clazz, @NotNull final Predicate<? super F> predicate) {
        return (it) -> clazz.isInstance(it) && predicate.test(clazz.cast(it));
    }

    @NotNull
    public IterationConditions<N> getConstraints() {
        return myConstraints;
    }

    public Predicate<? super N> getRecursion() {
        return myRecursion;
    }

    public Predicate<? super N> getFilter() {
        return myFilter;
    }

    @NotNull
    public TreeIterator<N> modifiedCopy(final @NotNull IterationConditions<N> constraints, final @NotNull Predicate<? super N> filter, final @NotNull Predicate<? super N> recursion) {
        return new TreeIterator<>(constraints, filter, recursion);
    }

    @NotNull
    public TreeIterator<N> reversed() {
        return modifiedCopy(myConstraints.getReversed(), myFilter, myRecursion);
    }

    @NotNull
    public TreeIterator<N> recursive() {
        //noinspection unchecked
        return modifiedCopy(myConstraints, myFilter, (Predicate<N>) TRUE);
    }

    @NotNull
    public TreeIterator<N> nonRecursive() {
        //noinspection unchecked
        return modifiedCopy(myConstraints, myFilter, (Predicate<N>) FALSE);
    }

    @NotNull
    public TreeIterator<N> recurse(final @NotNull Predicate<? super N> predicate) {
        return modifiedCopy(myConstraints, myFilter, it -> myRecursion.test(it) || predicate.test(it));
    }

    @NotNull
    public TreeIterator<N> recurse(final @NotNull Class clazz) {
        return recurse(getPredicate(clazz));
    }

    @NotNull
    public <F> TreeIterator<N> recurse(final @NotNull Class<F> clazz, @NotNull Predicate<? super F> predicate) {
        return recurse(getPredicate(clazz, predicate));
    }

    @NotNull
    public TreeIterator<N> noRecurse(final @NotNull Predicate<? super N> predicate) {
        return modifiedCopy(myConstraints, myFilter, it -> myRecursion.test(it) && !predicate.test(it));
    }

    @NotNull
    public TreeIterator<N> noRecurse(final @NotNull Class clazz) {
        return noRecurse(getPredicate(clazz));
    }

    @NotNull
    public <F> TreeIterator<N> noRecurse(final @NotNull Class<F> clazz, @NotNull Predicate<? super F> predicate) {
        return noRecurse(getPredicate(clazz, predicate));
    }

    @NotNull
    public TreeIterator<N> aborted() {
        return modifiedCopy(myConstraints.getAborted(), myFilter, myRecursion);
    }

    @NotNull
    public TreeIterator<N> filterOut(final @NotNull Predicate<? super N> predicate) {
        return modifiedCopy(myConstraints, it -> myFilter.test(it) && !predicate.test(it), myRecursion);
    }

    @NotNull
    public TreeIterator<N> filterOut(final @NotNull Class clazz) {
        return filterOut(getPredicate(clazz));
    }

    @NotNull
    public <F> TreeIterator<N> filterOut(final @NotNull Class<F> clazz, @NotNull Predicate<? super F> predicate) {
        return filterOut(getPredicate(clazz, predicate));
    }

    @NotNull
    public TreeIterator<N> filter(final @NotNull Predicate<? super N> predicate) {
        return modifiedCopy(myConstraints, it -> myFilter.test(it) && predicate.test(it), myRecursion);
    }

    @NotNull
    public TreeIterator<N> filter(final @NotNull Class clazz) {
        return filter(getPredicate(clazz));
    }

    @NotNull
    public <F> TreeIterator<N> filter(final @NotNull Class<F> clazz, @NotNull Predicate<? super F> predicate) {
        return filter(getPredicate(clazz, predicate));
    }

    @NotNull
    public static <N> TreeIterator<N> of(final @NotNull IterationConditions<N> constraints) {
        return new TreeIterator<>(constraints);
    }

    @NotNull
    public static <N> TreeIterator<N> of(final @NotNull IterationConditions<N> constraints, final @NotNull Predicate<? super N> filter) {
        return new TreeIterator<>(constraints, filter);
    }

    @NotNull
    public static <N> TreeIterator<N> of(final @NotNull IterationConditions<N> constraints, final @NotNull Predicate<? super N> filter, final @NotNull Predicate<? super N> recursion) {
        return new TreeIterator<>(constraints, filter, recursion);
    }

    @NotNull
    public static <N> Predicate<N> TRUE() {
        return n -> true;
    }

    @NotNull
    public static <N> Predicate<N> FALSE() {
        return n -> true;
    }

    public <R> ValueIteration<R> iterate(@NotNull N element, @NotNull R defaultValue, @NotNull ValueIterationConsumer<? super N, R> consumer) {
        final IteratorInstance<N, R> instance = new IteratorInstance<>(getConstraints(), getFilter(), getRecursion(), element, defaultValue);
        instance.iterate(consumer);
        return instance;
    }

    public <T, R> ValueIteration<R> iterate(@NotNull N element, @NotNull R defaultValue, @NotNull ValueIterationAdapter<? super N, T> adapter, @NotNull ValueIterationConsumer<? super T, R> consumer) {
        final IteratorInstance<N, R> instance = new IteratorInstance<>(getConstraints(), getFilter(), getRecursion(), element, defaultValue);
        instance.iterate(adapter.getConsumerAdapter().getConsumer(consumer));
        return instance;
    }

    public <R> VoidIteration iterate(@NotNull final N element, @NotNull final VoidIterationConsumer<? super N> consumer) {
        final IteratorInstance<N, R> instance = new IteratorInstance<>(getConstraints(), getFilter(), getRecursion(), element);
        instance.iterate(consumer);
        return instance;
    }

    public <T, R> VoidIteration iterate(@NotNull final N element, @NotNull ValueIterationAdapter<? super N, T> adapter, @NotNull final VoidIterationConsumer<? super T> consumer) {
        final IteratorInstance<N, R> instance = new IteratorInstance<>(getConstraints(), getFilter(), getRecursion(), element);
        instance.iterate(adapter.getConsumerAdapter().getConsumer(consumer));
        return instance;
    }

    @NotNull
    public <R> R doLoop(@NotNull N element, @NotNull R defaultValue, @NotNull ValueIterationConsumer<? super N, R> consumer) {
        return iterate(element, defaultValue, consumer).getResult();
    }

    public void doLoop(@NotNull N element, @NotNull VoidIterationConsumer<? super N> consumer) {
        iterate(element, consumer);
    }

    @NotNull
    public <T, R> R doLoop(@NotNull N element, @NotNull R defaultValue, @NotNull ValueIterationAdapter<? super N, T> adapter, @NotNull ValueIterationConsumer<? super T, R> consumer) {
        return iterate(element, defaultValue, adapter, consumer).getResult();
    }

    public <T, R> void doLoop(@NotNull N element, @NotNull ValueIterationAdapter<? super N, T> adapter, @NotNull VoidIterationConsumer<? super T> consumer) {
        iterate(element, adapter, consumer);
    }
}
