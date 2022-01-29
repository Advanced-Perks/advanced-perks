package de.fabilucius.advancedperks.commands;

import com.google.common.collect.Lists;
import org.bukkit.command.CommandSender;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

public abstract class SubCommand {

    private final String identifier;
    private final List<String> aliases;
    private final String permission;

    public SubCommand() {
        if (!this.getClass().isAnnotationPresent(Details.class)) {
            throw new IllegalStateException("Cannot construct SubCommand when no Details annotation is present.");
        }
        Details details = this.getClass().getAnnotation(Details.class);
        this.identifier = details.identifier();
        this.aliases = Lists.newArrayList(details.aliases());
        this.permission = details.permission();
    }

    public abstract void handleCommand(CommandSender commandSender, String... arguments);

    public abstract List<String> handleTabComplete(CommandSender commandSender, String... arguments);

    /* the getter and setter of this class */

    public String getIdentifier() {
        return identifier;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String getPermission() {
        return permission;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Details {

        String identifier();

        String[] aliases();

        String permission() default "";

    }

}
