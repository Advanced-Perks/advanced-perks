package de.fabilucius.advancedperks.commons.command.metadata;

import de.fabilucius.advancedperks.commons.command.command.AbstractCommand;
import de.fabilucius.advancedperks.commons.command.command.AbstractSubCommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The aliases are the alternate names an {@link AbstractCommand} or {@link AbstractSubCommand}
 * can be run.
 * The command classes will make sure that there is no duplicate among the aliases of a {@link AbstractCommand}'s
 * {@link AbstractSubCommand} to prevent sub command collision when executing instances of
 * {@link AbstractCommand}'s.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aliases {

    String[] value();

}
