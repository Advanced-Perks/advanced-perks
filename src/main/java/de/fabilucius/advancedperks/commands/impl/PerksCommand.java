package de.fabilucius.advancedperks.commands.impl;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commands.AbstractCommand;
import de.fabilucius.advancedperks.commands.SubCommand;
import de.fabilucius.advancedperks.commands.impl.subcommands.OpenSubCommand;
import de.fabilucius.advancedperks.commands.impl.subcommands.ToggleSubCommand;
import de.fabilucius.advancedperks.gui.PerkGuiWindow;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

@AbstractCommand.Details(identifier = "perks", subCommands = {OpenSubCommand.class, ToggleSubCommand.class})
public class PerksCommand extends AbstractCommand {
    @Override
    public void handleCommand(CommandSender commandSender, String... arguments) {
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
                    .map(SubCommand::getIdentifier)
                    .collect(Collectors.toList());
        } else {
            return this.getSubCommands().stream()
                    .map(SubCommand::getIdentifier)
                    .filter(subCommands -> subCommands.toLowerCase().startsWith(arguments[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
    }
}
