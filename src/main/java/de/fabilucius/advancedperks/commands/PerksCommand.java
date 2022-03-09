package de.fabilucius.advancedperks.commands;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commands.subcommands.OpenSubCommand;
import de.fabilucius.advancedperks.commands.subcommands.ToggleSubCommand;
import de.fabilucius.advancedperks.gui.PerkGuiWindow;
import de.fabilucius.sympel.command.types.AbstractCommand;
import de.fabilucius.sympel.command.types.AbstractSubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

@AbstractCommand.Details(identifier = "perks", subCommands = {OpenSubCommand.class, ToggleSubCommand.class})
public class PerksCommand extends AbstractCommand {

    public PerksCommand() {
        this.setNoPermissionMessage(AdvancedPerks.getMessageConfiguration().getMessage("Command.No-Permission"));
    }

    @Override
    public void handleCommandExecute(CommandSender commandSender, String... arguments) {
        if (commandSender instanceof Player) {
            if (arguments.length == 0) {
                Player player = (Player) commandSender;
                AdvancedPerks.getGuiManager().openGui(player, new PerkGuiWindow(player));
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
