package de.fabilucius.advancedperks.commons;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MapCache<K, V> implements Initializable {

    private final Map<K, V> cache = Maps.newHashMap();

    public MapCache() {
        this.initialize();
    }

    @Override
    public void initialize() {
    }


    public final Map.Entry<K, V> getEntry(Predicate<? super Map.Entry<K, V>> predicate) {
        return this.getCache().entrySet().stream().filter(predicate).findAny().orElse(null);
    }

    public final List<Map.Entry<K, V>> getEntries(Predicate<? super Map.Entry<K, V>> predicate) {
        return this.getCache().entrySet().stream().filter(predicate).collect(Collectors.toList());
    }

    /* the getter and setter of this class */

    public Map<K, V> getCache() {
        return cache;
    }
}
