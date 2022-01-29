package de.fabilucius.advancedperks.commons;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ListCache<T> implements Initializable {

    private final ArrayList<T> cache = Lists.newArrayList();

    public ListCache() {
        this.initialize();
    }

    @Override
    public void initialize() {
    }

    public final T getEntry(Predicate<T> predicate) {
        return this.getEntryWithDefault(predicate, null);
    }

    public final T getEntryWithDefault(Predicate<T> predicate, T defaultValue) {
        return this.getCache().stream().filter(predicate).findAny().orElse(defaultValue);
    }

    public final List<T> getEntries(Predicate<T> predicate) {
        return this.getCache().stream().filter(predicate).collect(Collectors.toList());
    }

    /* the getter and setter of this class */

    public ArrayList<T> getCache() {
        return cache;
    }
}
