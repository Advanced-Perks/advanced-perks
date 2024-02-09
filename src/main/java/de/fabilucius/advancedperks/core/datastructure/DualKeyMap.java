package de.fabilucius.advancedperks.core.datastructure;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;

public class DualKeyMap<F, S, V> {

    private final Map<F, V> dataMap = Maps.newHashMap();
    private final Map<S, F> keyIndexMap = Maps.newHashMap();

    public V getByFirstKey(F firstKey) {
        return this.dataMap.get(firstKey);
    }

    public V getBySecondKey(S secondKey) {
        F firstKey = this.keyIndexMap.get(secondKey);
        if (firstKey == null) {
            return null;
        }
        return this.dataMap.get(firstKey);
    }

    public V putValue(F firstKey, S secondKey, V value) {
        if (this.containsFirstKey(firstKey)) {
            this.removeByFirstKey(firstKey);
        } else if (this.containsSecondKey(secondKey)) {
            this.removeBySecondKey(secondKey);
        }
        this.keyIndexMap.put(secondKey, firstKey);
        return this.dataMap.put(firstKey, value);
    }

    public boolean containsFirstKey(F firstKey) {
        return this.dataMap.containsKey(firstKey);
    }

    public boolean containsSecondKey(S secondKey) {
        return this.keyIndexMap.containsKey(secondKey);
    }

    public V removeByFirstKey(F firstKey) {
        this.keyIndexMap.entrySet().removeIf(sfEntry -> sfEntry.getValue().equals(firstKey));
        return this.dataMap.remove(firstKey);
    }

    public V removeBySecondKey(S secondKey) {
        F firstKey = this.keyIndexMap.get(secondKey);
        if (firstKey == null) {
            return null;
        }
        this.keyIndexMap.remove(secondKey);
        return this.dataMap.remove(firstKey);
    }

    public Collection<V> values() {
        return this.dataMap.values();
    }

    public int size() {
        return this.dataMap.size();
    }

}