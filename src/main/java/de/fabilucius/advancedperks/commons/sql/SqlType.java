package de.fabilucius.advancedperks.commons.sql;

import java.util.Arrays;

public enum SqlType {

    FILE,
    DATABASE;

    public static SqlType getSqlTypeByName(String name) {
        return Arrays.stream(values()).filter(sqlType -> sqlType.name().equalsIgnoreCase(name)).findAny().orElse(FILE);
    }

}
