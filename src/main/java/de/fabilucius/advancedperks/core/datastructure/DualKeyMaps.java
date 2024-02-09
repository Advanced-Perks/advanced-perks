package de.fabilucius.advancedperks.core.datastructure;

import org.checkerframework.checker.nullness.qual.Nullable;

public final class DualKeyMaps {

    private DualKeyMaps() {
    }

    public static <F extends @Nullable Object, S extends @Nullable Object, V extends @Nullable Object> DualKeyMap<F, S, V> newDualKeyMap() {
        return new DualKeyMap<>();
    }

}
