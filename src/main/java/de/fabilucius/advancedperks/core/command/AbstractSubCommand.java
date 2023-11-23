package de.fabilucius.advancedperks.core.command;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import de.fabilucius.advancedperks.configuration.ConfigurationProvider;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationInitializationException;
import de.fabilucius.advancedperks.core.MessagesConfiguration;
import de.fabilucius.advancedperks.core.command.annotation.Aliases;
import de.fabilucius.advancedperks.core.command.annotation.CommandIdentifier;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public abstract class AbstractSubCommand {

    protected final MessagesConfiguration messagesConfiguration;
    private final String identifier;

    @Inject
    public AbstractSubCommand(ConfigurationProvider configurationProvider) throws ConfigurationInitializationException {
        this.messagesConfiguration = configurationProvider.getConfigurationAndLoad(MessagesConfiguration.class);
        CommandIdentifier commandIdentifierAnnotation = this.getClass().getAnnotation(CommandIdentifier.class);
        if (commandIdentifierAnnotation == null) {
            throw new IllegalStateException("The sub command class %s is missing a @Identifier annotation.");
        }
        this.identifier = commandIdentifierAnnotation.value();
    }

    public String getIdentifier() {
        return identifier;
    }

    @NotNull
    public final Optional<List<String>> getAliases() {
        if (this.getClass().isAnnotationPresent(Aliases.class)) {
            return Optional.of(Lists.newArrayList(this.getClass().getAnnotation(Aliases.class).value()));
        }
        return Optional.empty();
    }

    public Optional<String> getPermission() {
        return Optional.empty();
    }

    public abstract void executeCommand(CommandSender commandSender, String... arguments);

    public abstract List<String> tabComplete(CommandSender commandSender, String... arguments);

}
