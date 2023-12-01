package de.fabilucius.advancedperks.core.command;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import de.fabilucius.advancedperks.configuration.ConfigurationLoader;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationInitializationException;
import de.fabilucius.advancedperks.core.MessagesConfiguration;
import de.fabilucius.advancedperks.core.command.annotation.Aliases;
import de.fabilucius.advancedperks.core.command.annotation.CommandIdentifier;
import de.fabilucius.advancedperks.core.command.annotation.Permission;
import de.fabilucius.advancedperks.core.command.annotation.SubCommands;
import de.fabilucius.advancedperks.core.logging.APLogger;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

public abstract class AbstractCommand implements CommandExecutor, TabCompleter {


    private final Injector injector;

    private final APLogger logger;

    private final String identifier;
    private final MessagesConfiguration messagesConfiguration;

    private final List<AbstractSubCommand> subCommands = Lists.newArrayList();

    //TODO currently unneeded figure out a stable way for auto register without adding command to plugin.yml
    @Inject
    public AbstractCommand(ConfigurationLoader configurationLoader, APLogger logger, Injector injector) throws ConfigurationInitializationException {
        this.messagesConfiguration = configurationLoader.getConfigurationAndLoad(MessagesConfiguration.class);
        this.logger = logger;
        this.injector = injector;
        CommandIdentifier commandIdentifierAnnotation = this.getClass().getAnnotation(CommandIdentifier.class);
        if (commandIdentifierAnnotation == null) {
            throw new IllegalStateException("Tried to initialize the command %s but there is not @Identifier annotation present.".formatted(this.getClass().getName()));
        }
        this.identifier = commandIdentifierAnnotation.value();
        PluginCommand pluginCommand = Bukkit.getPluginCommand(this.identifier);
        if (pluginCommand == null) {
            throw new IllegalStateException("Tried to initialize the command %s but it seems it wasn't added to the plugin.yml file.".formatted(this.getClass().getName()));
        }
        if (this.getClass().isAnnotationPresent(Aliases.class)) {
            pluginCommand.setAliases(Arrays.stream(this.getClass().getAnnotation(Aliases.class).value()).toList());
        }
        pluginCommand.setExecutor(this);
        pluginCommand.setTabCompleter(this);
        this.registerSubCommands();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (this.getPermission().isPresent() && !sender.hasPermission(this.getPermission().get())) {
            sender.sendMessage(this.getPermissionMessage());
            return false;
        }
        /* Check for potential sub command invocation */
        if (args.length > 0) {
            Optional<AbstractSubCommand> potentialSubCommand = this.getSubCommand(args[0]);
            if (potentialSubCommand.isPresent()) {
                AbstractSubCommand subCommand = potentialSubCommand.get();
                if (subCommand.getPermission().isPresent() && !sender.hasPermission(subCommand.getPermission().get())) {
                    sender.sendMessage(this.getPermissionMessage());
                    return false;
                }
                subCommand.executeCommand(sender, Arrays.copyOfRange(args, 1, args.length));
                return true;
            }
        }
        this.executeCommand(sender, args);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (this.getPermission().isPresent() && !sender.hasPermission(this.getPermission().get())) {
            return null;
        }
        if (args.length > 0) {
            Optional<AbstractSubCommand> potentialSubCommand = this.getSubCommand(args[0]);
            if (potentialSubCommand.isPresent()) {
                return potentialSubCommand.get().tabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
            }
            return args.length == 1 ? this.subCommands.stream()
                    .filter(subCommand -> subCommand.getPermission().isEmpty() ||
                            sender.hasPermission(subCommand.getPermission().get()))
                    .map(AbstractSubCommand::getIdentifier)
                    .filter(subCommand -> subCommand.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList()) : Lists.newArrayList();
        }
        return this.tabComplete(sender, args);
    }

    private void registerSubCommands() {
        if (this.getClass().isAnnotationPresent(SubCommands.class)) {
            /* List to check for duplicate identifier or aliases in the subcommands */
            List<String> aliases = Lists.newArrayList();
            Arrays.stream(this.getClass().getAnnotation(SubCommands.class).value()).forEach(subCommandClass -> {
                try {
                    AbstractSubCommand abstractSubCommand = this.injector.getInstance(subCommandClass);//subCommandClass.getConstructor().newInstance();
                    if (aliases.contains(abstractSubCommand.getIdentifier()) || (abstractSubCommand.getAliases().isPresent() && !Collections.disjoint(aliases, abstractSubCommand.getAliases().get()))) {
                        throw new IllegalStateException(String.format("Cannot load sub command %s in command %s, the sub command contains an " +
                                "identifier or alias that another sub command already contains.", subCommandClass.getName(), this.getClass().getName()));
                    }
                    aliases.add(abstractSubCommand.getIdentifier());
                    this.subCommands.add(abstractSubCommand);
                } catch (Exception exception) {
                    this.logger.log(Level.WARNING, String.format("Error while loading sub command %s in command %s.",
                            subCommandClass.getName(), this.getClass().getName()), exception);
                }
            });
        }
    }

    public Optional<AbstractSubCommand> getSubCommand(String alias) {
        return this.subCommands.stream().filter(abstractSubCommand -> abstractSubCommand.getIdentifier().equalsIgnoreCase(alias) ||
                        (abstractSubCommand.getAliases().isPresent() && abstractSubCommand.getAliases().get().stream().anyMatch(s -> s.equalsIgnoreCase(alias))))
                .findFirst();
    }

    public String getPermissionMessage() {
        return this.messagesConfiguration.getComputedString("command." + this.identifier + ".no_permission");
    }

    public Optional<String> getPermission() {
        return this.getClass().isAnnotationPresent(Permission.class) ?
                Optional.of(this.getClass().getAnnotation(Permission.class).value()) : Optional.empty();
    }

    public abstract void executeCommand(CommandSender commandSender, String... arguments);

    public abstract List<String> tabComplete(CommandSender commandSender, String... arguments);

}
