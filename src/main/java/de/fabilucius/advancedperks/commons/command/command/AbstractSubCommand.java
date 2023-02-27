package de.fabilucius.advancedperks.commons.command.command;

import com.google.common.collect.Lists;
import de.fabilucius.advancedperks.commons.command.metadata.Aliases;
import de.fabilucius.advancedperks.commons.command.metadata.NoPermissionMessage;
import de.fabilucius.advancedperks.commons.command.metadata.Permission;
import de.fabilucius.advancedperks.commons.command.utilities.CommandEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public abstract class AbstractSubCommand implements CommandEntity {

    /**
     * The main identifier that is needed for the subcommand to be called from a main command.
     * This needs to be unique from all other identifier or aliases of the subcommands a command tries to register.
     */
    private final String identifier;
    private List<String> aliases;
    private String noPermissionMessage;
    private String permission;

    public AbstractSubCommand(String identifier) {
        this.identifier = identifier;
        this.getAliases().ifPresent(aliases -> this.aliases = aliases);
        this.getNoPermissionMessage().ifPresent(noPermissionMessage -> this.noPermissionMessage = noPermissionMessage);
        this.getPermission().ifPresent(permission -> this.permission = permission);
    }

    /* the getter and setter of the class */

    @NotNull
    public final String getIdentifier() {
        return identifier;
    }

    @NotNull
    public final Optional<List<String>> getAliases() {
        if (this.aliases != null) {
            return Optional.of(this.aliases);
        }
        if (this.getClass().isAnnotationPresent(Aliases.class)) {
            return Optional.of(Lists.newArrayList(this.getClass().getAnnotation(Aliases.class).value()));
        }
        return Optional.empty();
    }

    public final void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    @NotNull
    public final Optional<String> getNoPermissionMessage() {
        if (this.noPermissionMessage != null) {
            return Optional.of(this.noPermissionMessage);
        }
        if (this.getClass().isAnnotationPresent(NoPermissionMessage.class)) {
            return Optional.of(this.getClass().getAnnotation(NoPermissionMessage.class).value());
        }
        return Optional.empty();
    }

    public final void setNoPermissionMessage(String noPermissionMessage) {
        this.noPermissionMessage = noPermissionMessage;
    }

    @NotNull
    public final Optional<String> getPermission() {
        if (this.permission != null) {
            return Optional.of(this.permission);
        }
        if (this.getClass().isAnnotationPresent(Permission.class)) {
            return Optional.of(this.getClass().getAnnotation(Permission.class).value());
        }
        return Optional.empty();
    }

    public final void setPermission(String permission) {
        this.permission = permission;
    }

}
