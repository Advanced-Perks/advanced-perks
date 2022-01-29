package de.fabilucius.advancedperks.utilities;

import java.util.Iterator;
import java.util.function.BiConsumer;

public class IterableUtilities {
    public static <T, U> void iterateSimultaneously(Iterable<T> first, Iterable<U> second, BiConsumer<T, U> consumer) {
        Iterator<T> firstIterator = first.iterator();
        Iterator<U> secondIterator = second.iterator();
        while (firstIterator.hasNext() && secondIterator.hasNext()) {
            consumer.accept(firstIterator.next(), secondIterator.next());
        }
    }
}
