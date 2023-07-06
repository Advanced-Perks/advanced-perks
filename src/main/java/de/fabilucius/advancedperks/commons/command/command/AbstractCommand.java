package de.fabilucius.advancedperks.commons.command.command;

import com.google.common.collect.Lists;
import de.fabilucius.advancedperks.commons.command.exception.CommandInitializationException;
import de.fabilucius.advancedperks.commons.command.metadata.*;
import de.fabilucius.advancedperks.commons.command.utilities.CommandEntity;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class AbstractCommand implements CommandEntity, CommandExecutor, TabCompleter {

    private static final Logger LOGGER = Bukkit.getLogger();

    private final PluginCommand pluginCommand;

    /**
     * The list of {@link AbstractSubCommand} that are related to this command.
     * This list can also be empty.
     */
    private final List<AbstractSubCommand> abstractSubCommands = Lists.newArrayList();

    /**
     * The identifier of the command which also is used to get its instance from bukkit.
     */
    private final String identifier;

    public AbstractCommand(String commandIdentifier) {
        this.identifier = commandIdentifier;

        this.pluginCommand = Bukkit.getPluginCommand(this.identifier);
        if (pluginCommand == null) {
            throw new CommandInitializationException(String.format("Cannot create an instance of %s, %s seems to be missing inside the plugin.yml.",
                    this.getClass().getName(), this.identifier));
        }

        this.pluginCommand.setExecutor(this);
        this.pluginCommand.setTabCompleter(this);
        /* Filling in the metadata of the bukkit command instance */
        this.getAliases().ifPresent(this.pluginCommand::setAliases);
        this.getNoPermissionMessage().ifPresent(this.pluginCommand::setPermissionMessage);
        this.getPermission().ifPresent(this.pluginCommand::setPermission);
        this.getDescription().ifPresent(this.pluginCommand::setDescription);
        this.getUsage().ifPresent(this.pluginCommand::setUsage);

        /* Registering the subcommands of the command if any are present */
        this.registerSubCommands();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length > 0) {
            Optional<AbstractSubCommand> potentialSubCommand = this.getSubCommandByAlias(strings[0]);
            if (potentialSubCommand.isPresent()) {
                AbstractSubCommand abstractSubCommand = potentialSubCommand.get();
                if (abstractSubCommand.getPermission().isPresent() &&
                        !commandSender.hasPermission(abstractSubCommand.getPermission().get())) {
                    if (abstractSubCommand.getNoPermissionMessage().isPresent()) {
                        commandSender.sendMessage(abstractSubCommand.getNoPermissionMessage().get());
                    } else {
                        if (command.getPermissionMessage() != null) {
                            commandSender.sendMessage(command.getPermissionMessage());
                        }
                    }
                    return false;
                }
                abstractSubCommand.handleCommandExecute(commandSender, Arrays.copyOfRange(strings, 1, strings.length));
                return false;
            }
        }
        this.handleCommandExecute(commandSender, strings);
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length > 0) {
            Optional<AbstractSubCommand> potentialSubCommand = this.getSubCommandByAlias(strings[0]);
            if (potentialSubCommand.isPresent()) {
                return potentialSubCommand.get().handleTabComplete(commandSender, Arrays.copyOfRange(strings, 1, strings.length));
            }
            return abstractSubCommands.stream()
                    .filter(subCommand -> !subCommand.getPermission().isPresent() ||
                            commandSender.hasPermission(subCommand.getPermission().get()))
                    .map(AbstractSubCommand::getIdentifier)
                    .filter(subCommand -> subCommand.toLowerCase().startsWith(strings[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return this.handleTabComplete(commandSender, strings);
    }

    /**
     * This method will check if the command class has a subcommands annotation present, and if so it will try to load
     * the subcommands from the annotation to the plugins subcommands cache to make them usable.
     * Trying to register subcommands with an identifier or alias that is already present on another subcommand on this command
     * will make registering the command fail.
     */
    private void registerSubCommands() {
        if (this.getClass().isAnnotationPresent(SubCommands.class)) {
            List<String> aliases = Lists.newArrayList();
            Arrays.stream(this.getClass().getAnnotation(SubCommands.class).value()).forEach(subCommandClass -> {
                try {
                    AbstractSubCommand abstractSubCommand = subCommandClass.getConstructor().newInstance();
                    if (aliases.contains(abstractSubCommand.getIdentifier()) || (abstractSubCommand.getAliases().isPresent() && !Collections.disjoint(aliases, abstractSubCommand.getAliases().get()))) {
                        throw new CommandInitializationException(String.format("Cannot load sub command %s in command %s, the sub command contains an " +
                                "identifier or alias that another sub command already contains.", subCommandClass.getName(), this.getClass().getName()));
                    }
                    aliases.add(abstractSubCommand.getIdentifier());
                    abstractSubCommand.getAliases().ifPresent(aliases::addAll);
                    this.abstractSubCommands.add(abstractSubCommand);
                } catch (Exception exception) {
                    LOGGER.log(Level.WARNING, String.format("Error while loading sub command %s in command %s:",
                            subCommandClass.getName(), this.getClass().getName()), exception);
                }
            });
        }
    }

    /**
     * This method will try to find any subcommand that has an identifier or an alias that can be associated with the String passed
     * as parameter of this method.
     *
     * @param subCommand the name of the identifier or alias of the subcommand to find
     * @return an optional where when a subcommand was found contains the subcommand or an empty optional if no subcommand was found
     */
    public final Optional<AbstractSubCommand> getSubCommandByAlias(String subCommand) {
        return this.abstractSubCommands.stream()
                .filter(abstractSubCommand -> {
                    return abstractSubCommand.getIdentifier().equalsIgnoreCase(subCommand) ||
                            (abstractSubCommand.getAliases().isPresent() && abstractSubCommand.getAliases().get().contains(subCommand));
                })
                .findFirst();
    }

    /* The getter and setter of this class */

    @NotNull
    public final String getIdentifier() {
        return identifier;
    }

    @NotNull
    public List<AbstractSubCommand> getSubCommands() {
        return abstractSubCommands;
    }

    @NotNull
    public final PluginCommand getPluginCommand() {
        return pluginCommand;
    }

    @NotNull
    public final Optional<String> getDescription() {
        if (this.getClass().isAnnotationPresent(Description.class)) {
            return Optional.of(this.getClass().getAnnotation(Description.class).value());
        }
        return Optional.empty();
    }

    public final void setDescription(String description) {
        this.pluginCommand.setDescription(description);
    }

    @NotNull
    public final Optional<List<String>> getAliases() {
        if (this.getClass().isAnnotationPresent(Aliases.class)) {
            return Optional.of(Lists.newArrayList(this.getClass().getAnnotation(Aliases.class).value()));
        }
        return Optional.empty();
    }

    public final void setAliases(List<String> aliases) {
        this.pluginCommand.setAliases(aliases);
    }

    @NotNull
    public final Optional<String> getNoPermissionMessage() {
        if (this.getClass().isAnnotationPresent(NoPermissionMessage.class)) {
            return Optional.of(this.getClass().getAnnotation(NoPermissionMessage.class).value());
        }
        return Optional.empty();
    }

    public final void setNoPermissionMessage(String noPermissionMessage) {
        this.pluginCommand.setPermissionMessage(noPermissionMessage);
    }

    @NotNull
    public final Optional<String> getPermission() {
        if (this.getClass().isAnnotationPresent(Permission.class)) {
            return Optional.of(this.getClass().getAnnotation(Permission.class).value());
        }
        return Optional.empty();
    }

    public final void setPermission(String permission) {
        this.pluginCommand.setPermission(permission);
    }

    @NotNull
    public final Optional<String> getUsage() {
        if (this.getClass().isAnnotationPresent(Usage.class)) {
            return Optional.of(this.getClass().getAnnotation(Usage.class).value());
        }
        return Optional.empty();
    }

    public final void setUsage(String usage) {
        this.pluginCommand.setUsage(usage);
    }

}
