package de.fabilucius.advancedperks.commons.command.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Usage is another bukkit related metadata a command can have.
 * It will show the correct usage of a command when used wrong.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Usage {

    String value();

}
