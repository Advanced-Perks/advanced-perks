package de.fabilucius.advancedperks.command;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import de.fabilucius.advancedperks.command.subcommands.BuySubCommand;
import de.fabilucius.advancedperks.command.subcommands.DisableSubCommand;
import de.fabilucius.advancedperks.command.subcommands.EnableSubCommand;
import de.fabilucius.advancedperks.command.subcommands.InfoSubCommand;
import de.fabilucius.advancedperks.command.subcommands.MigrateSubCommand;
import de.fabilucius.advancedperks.command.subcommands.ToggleSubCommand;
import de.fabilucius.advancedperks.core.configuration.ConfigurationLoader;
import de.fabilucius.advancedperks.core.configuration.exception.ConfigurationInitializationException;
import de.fabilucius.advancedperks.core.MessagesConfiguration;
import de.fabilucius.advancedperks.core.SettingsConfiguration;
import de.fabilucius.advancedperks.core.command.AbstractCommand;
import de.fabilucius.advancedperks.core.command.annotation.Aliases;
import de.fabilucius.advancedperks.core.command.annotation.CommandIdentifier;
import de.fabilucius.advancedperks.core.command.annotation.SubCommands;
import de.fabilucius.advancedperks.core.guisystem.GuiSystemManager;
import de.fabilucius.advancedperks.core.logging.APLogger;
import de.fabilucius.advancedperks.guisystem.perkgui.PerkGuiWindow;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.logging.Level;

@CommandIdentifier("perks")
@Aliases({"perk", "advancedperks"})
@SubCommands({InfoSubCommand.class, ToggleSubCommand.class, EnableSubCommand.class, DisableSubCommand.class, BuySubCommand.class, MigrateSubCommand.class})
public class PerksCommand extends AbstractCommand {

    @Inject
    private GuiSystemManager guiSystemManager;

    @Inject
    private APLogger logger;

    private final ConfigurationLoader configurationLoader;

    @Inject
    public PerksCommand(ConfigurationLoader configurationLoader, APLogger logger, Injector injector) throws ConfigurationInitializationException {
        super(configurationLoader, logger, injector);
        this.configurationLoader = configurationLoader;
    }

    @Override
    public void executeCommand(CommandSender commandSender, String... arguments) {
        try {
            this.guiSystemManager.registerGuiWindowAnOpen(new PerkGuiWindow(this.configurationLoader, this.configurationLoader.getConfigurationAndLoad(SettingsConfiguration.class), this.configurationLoader.getConfigurationAndLoad(MessagesConfiguration.class), (Player) commandSender), (Player) commandSender);
        } catch (ConfigurationInitializationException e) {
            commandSender.sendMessage(ChatColor.RED + "An error occurred while using this command contact the server administrator.");
            this.logger.log(Level.SEVERE, "An error occurred while opening the perks gui for %s.".formatted(commandSender.getName()), e);
        }
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, String... arguments) {
        return Lists.newArrayList();
    }
}
