package de.fabilucius.advancedperks.commons.command.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This metadata will tell the annotated (in this case a command or subcommand) which permission is required to run it.
 * It is used for automated permission checking when present on a command or subcommand.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {

    String value();

}
