package de.fabilucius.advancedperks.commands;

import com.google.common.collect.Lists;
import de.fabilucius.advancedperks.utilities.MessageConfigReceiver;
import org.bukkit.Bukkit;
import org.bukkit.command.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class AbstractCommand implements CommandExecutor, TabCompleter {

    private static final Logger LOGGER = Bukkit.getLogger();
    private final List<SubCommand> subCommands = Lists.newArrayList();
    private final String identifier;
    private final String permission;

    public AbstractCommand() {
        if (!this.getClass().isAnnotationPresent(Details.class)) {
            throw new IllegalStateException("Cannot construct AbstractCommand when no Details Annotation is present.");
        }
        Details details = this.getClass().getAnnotation(Details.class);
        this.identifier = details.identifier();
        this.permission = details.permission();
        Arrays.stream(details.subCommands()).forEach(subCommand -> {
            try {
                SubCommand instance = subCommand.getDeclaredConstructor().newInstance();
                this.getSubCommands().add(instance);
            } catch (Exception exception) {
                LOGGER.log(Level.SEVERE, "Error while adding a SubCommand to an AbstractCommand:", exception);
            }
        });
        this.checkSubCommandAliasesIntegrity();
        PluginCommand pluginCommand = Bukkit.getPluginCommand(this.getIdentifier());
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        }
    }

    private void checkSubCommandAliasesIntegrity() {
        List<String> aliases = Lists.newArrayList();
        this.getSubCommands().forEach(subCommand -> aliases.addAll(subCommand.getAliases()));
        if (aliases.stream().distinct().count() != aliases.size()) {
            throw new IllegalStateException("It seems that the SubCommands of the command " + this.getClass().getSimpleName() + " have multiple different SubCommands registering the same alias");
        }
    }

    public abstract void handleCommand(CommandSender commandSender, String... arguments);

    public abstract List<String> handleTabComplete(CommandSender commandSender, String... arguments);

    @Override
    public final boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (this.getPermission() != null && !this.getPermission().isEmpty() && !commandSender.hasPermission(this.getPermission())) {
            commandSender.sendMessage(MessageConfigReceiver.getMessage("Command.No-Permission"));
            return false;
        }
        if (strings.length > 0) {
            SubCommand subCommand = this.getSubCommands().stream().filter(sCommand -> sCommand.getIdentifier().equalsIgnoreCase(strings[0]) ||
                    sCommand.getAliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(strings[0]))).findAny().orElse(null);
            if (subCommand != null) {
                if (!commandSender.hasPermission(subCommand.getPermission())) {
                    commandSender.sendMessage(MessageConfigReceiver.getMessage("Command.No-Permission"));
                    return false;
                }
                subCommand.handleCommand(commandSender, Arrays.copyOfRange(strings, 1, strings.length));
                return false;
            }
        }
        this.handleCommand(commandSender, strings);
        return false;
    }

    @Override
    public final List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length > 0) {
            SubCommand possibleSubCommand = this.getSubCommands().stream()
                    .filter(subCommand -> subCommand.getIdentifier().equalsIgnoreCase(strings[0]))
                    .findFirst()
                    .orElse(null);
            if (possibleSubCommand != null) {
                return possibleSubCommand.handleTabComplete(commandSender, Arrays.copyOfRange(strings, 1, strings.length));
            }
            return this.getSubCommands().stream()
                    .map(SubCommand::getIdentifier)
                    .filter(subCommand -> subCommand.toLowerCase().startsWith(strings[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return this.handleTabComplete(commandSender, strings);
    }

    /* the getter and setter of this class */

    public String getIdentifier() {
        return identifier;
    }

    public String getPermission() {
        return permission;
    }

    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Details {

        String identifier();

        String permission() default "";

        Class<? extends SubCommand>[] subCommands() default {};

    }
}
