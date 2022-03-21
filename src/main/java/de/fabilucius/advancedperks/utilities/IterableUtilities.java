package de.fabilucius.advancedperks.utilities;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.BiConsumer;

public class IterableUtilities {

    /**
     * This method solves the problem of being unable to loop through two iterable in parallel.
     * Interacting with the iterable's can be done through the bi consumer.
     * Important to notice is that if one iterable has more element than the other one the iteration through both will stop as soon as
     * the first list hits its end.
     *
     * @param first    the first iterable to loop through
     * @param second   the second iterable to loop through
     * @param consumer the consumer to interact with the looped data
     * @param <T>      the first generic type of the iterable
     * @param <U>      the second generic type of the iterable
     */
    public static <T, U> void iterateSimultaneously(@NotNull Iterable<T> first, @NotNull Iterable<U> second, @NotNull BiConsumer<T, U> consumer) {
        Preconditions.checkNotNull(first, "first iterable cannot be null.");
        Preconditions.checkNotNull(second, "second iterable cannot be null.");
        Preconditions.checkNotNull(consumer, "consumer cannot be null.");
        
        Iterator<T> firstIterator = first.iterator();
        Iterator<U> secondIterator = second.iterator();
        while (firstIterator.hasNext() && secondIterator.hasNext()) {
            consumer.accept(firstIterator.next(), secondIterator.next());
        }
    }
}
