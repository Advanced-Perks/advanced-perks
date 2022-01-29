package de.fabilucius.advancedperks.commons;

import java.util.Arrays;
import java.util.Objects;

public class NullSafety {

    public static boolean isAnyNull(Object... objects) {
        return Arrays.stream(objects).anyMatch(Objects::isNull);
    }

}
