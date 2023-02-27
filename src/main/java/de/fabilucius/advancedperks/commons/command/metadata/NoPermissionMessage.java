package de.fabilucius.advancedperks.commons.command.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link NoPermissionMessage} is the data a command can hold that will show a message to the user, when he tries
 * to run a command that is annotated by it but doesn't have the appropriate permission to do it.
 * In case this annotation isn't present the default bukkit message will show.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoPermissionMessage {

    String value();

}
