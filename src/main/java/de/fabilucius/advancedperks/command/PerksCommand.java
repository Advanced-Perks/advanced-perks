package de.fabilucius.advancedperks.command;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import de.fabilucius.advancedperks.command.subcommands.DisableSubCommand;
import de.fabilucius.advancedperks.command.subcommands.EnableSubCommand;
import de.fabilucius.advancedperks.command.subcommands.InfoSubCommand;
import de.fabilucius.advancedperks.configuration.ConfigurationProvider;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationInitializationException;
import de.fabilucius.advancedperks.core.command.AbstractCommand;
import de.fabilucius.advancedperks.core.command.annotation.Aliases;
import de.fabilucius.advancedperks.core.command.annotation.CommandIdentifier;
import de.fabilucius.advancedperks.core.command.annotation.SubCommands;
import de.fabilucius.advancedperks.core.logging.APLogger;
import org.bukkit.command.CommandSender;

import java.util.List;

@CommandIdentifier("perks")
@Aliases({"perk", "advancedperks"})
@SubCommands({InfoSubCommand.class, EnableSubCommand.class, DisableSubCommand.class})
public class PerksCommand extends AbstractCommand {
    @Inject
    public PerksCommand(ConfigurationProvider configurationProvider, APLogger logger, Injector injector) throws ConfigurationInitializationException {
        super(configurationProvider, logger, injector);
    }

    @Override
    public void executeCommand(CommandSender commandSender, String... arguments) {
        //TODO open gui
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, String... arguments) {
        return Lists.newArrayList();
    }
}
