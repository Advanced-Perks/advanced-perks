package de.fabilucius.advancedperks.commands;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commands.subcommands.*;
import de.fabilucius.advancedperks.commons.command.command.AbstractCommand;
import de.fabilucius.advancedperks.commons.command.command.AbstractSubCommand;
import de.fabilucius.advancedperks.commons.command.metadata.SubCommands;
import de.fabilucius.advancedperks.gui.PerkGuiWindow;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

@SubCommands({OpenSubCommand.class, ToggleSubCommand.class, BuySubCommand.class, ReloadSubCommand.class, InfoSubCommand.class})
public class PerksCommand extends AbstractCommand {

    public PerksCommand() {
        super("perks");
        this.setNoPermissionMessage(AdvancedPerks.getInstance().getMessageConfiguration().getMessage("Command.No-Permission"));
    }

    @Override
    public void handleCommandExecute(CommandSender commandSender, String... arguments) {
        if (commandSender instanceof Player) {
            if (arguments.length == 0) {
                Player player = (Player) commandSender;
                AdvancedPerks.getInstance().getGuiManager().openGui(player, new PerkGuiWindow(player));
            }
        }
    }

    @Override
    public List<String> handleTabComplete(CommandSender commandSender, String... arguments) {
        if (arguments.length == 0 || arguments[0].length() == 1) {
            return this.getSubCommands().stream()
                    .map(AbstractSubCommand::getIdentifier)
                    .collect(Collectors.toList());
        } else {
            return this.getSubCommands().stream()
                    .map(AbstractSubCommand::getIdentifier)
                    .filter(subCommands -> subCommands.toLowerCase().startsWith(arguments[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
    }
}
