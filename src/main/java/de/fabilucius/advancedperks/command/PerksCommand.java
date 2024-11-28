package de.fabilucius.advancedperks.command;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import de.fabilucius.advancedperks.command.subcommands.*;
import de.fabilucius.advancedperks.core.command.AbstractCommand;
import de.fabilucius.advancedperks.core.command.annotation.Aliases;
import de.fabilucius.advancedperks.core.command.annotation.CommandIdentifier;
import de.fabilucius.advancedperks.core.command.annotation.SubCommands;
import de.fabilucius.advancedperks.core.configuration.type.MessageConfiguration;
import de.fabilucius.advancedperks.core.configuration.type.SettingsConfiguration;
import de.fabilucius.advancedperks.core.guisystem.GuiSystemManager;
import de.fabilucius.advancedperks.core.logging.APLogger;
import de.fabilucius.advancedperks.guisystem.perkgui.PerkGuiWindow;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@CommandIdentifier("perks")
@Aliases({"perk", "advancedperks"})
@SubCommands({GuiSubCommand.class, InfoSubCommand.class, ToggleSubCommand.class, EnableSubCommand.class, DisableSubCommand.class, BuySubCommand.class, MigrateSubCommand.class, SetPriceSubCommand.class})
public class PerksCommand extends AbstractCommand {

    @Inject
    private GuiSystemManager guiSystemManager;

    @Inject
    private APLogger logger;

    @Inject
    private SettingsConfiguration settingsConfiguration;

    private final MessageConfiguration messageConfiguration;

    @Inject
    public PerksCommand(MessageConfiguration messageConfiguration, APLogger logger, Injector injector) {
        super(messageConfiguration, logger, injector);
        this.messageConfiguration = messageConfiguration;
    }

    @Override
    public void executeCommand(CommandSender commandSender, String... arguments) {
        this.guiSystemManager.registerGuiWindowAnOpen(new PerkGuiWindow(settingsConfiguration, messageConfiguration, (Player) commandSender), (Player) commandSender);
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, String... arguments) {
        return Lists.newArrayList();
    }
}
