package de.fabilucius.advancedperks.commons.command.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The description is another metadata that bukkit commands can have but are not required to.
 * It will tell when f.E. using the /help command, in a human friendly way what the command is supposed to do.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {

    String value();

}
