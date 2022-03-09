package de.fabilucius.advancedperks.commons;

import com.google.common.base.Preconditions;

import java.util.Arrays;

public class NullSafety {

    public static void validateNotNull(Object... objects) {
        Arrays.stream(objects)
                .forEach(object -> Preconditions.checkNotNull(object, object.getClass().getSimpleName() + "cannot be null"));
    }

}
