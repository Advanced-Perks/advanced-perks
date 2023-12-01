package de.fabilucius.advancedperks.core.command.annotation;

import de.fabilucius.advancedperks.core.command.AbstractSubCommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommands {

    Class<? extends AbstractSubCommand>[] value();

}
