package de.fabilucius.advancedperks.commons.command.metadata;

import de.fabilucius.advancedperks.commons.command.command.AbstractSubCommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This metadata will contain all subcommands of a main command.
 * It is used to load all subcommands to the cache of th command.
 * The absent of this annotation just means that the command doesn't have any subcommands.
 * Annotating a subcommand with this annotation will not have any effect.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommands {

    Class<? extends AbstractSubCommand>[] value();

}
