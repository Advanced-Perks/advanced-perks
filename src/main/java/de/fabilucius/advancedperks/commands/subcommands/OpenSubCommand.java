package de.fabilucius.advancedperks.commands.subcommands;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.gui.PerkGuiWindow;
import de.fabilucius.sympel.command.types.AbstractSubCommand;
import de.fabilucius.sympel.configuration.utilities.ReplaceLogic;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AbstractSubCommand.Details(identifier = "open", permission = "advancedperks.command.open", aliases = {"o"})
public class OpenSubCommand extends AbstractSubCommand {
    @Override
    public void handleCommandExecute(CommandSender commandSender, String... arguments) {
        if (arguments.length == 0) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                AdvancedPerks.getGuiManager().openGui(player, new PerkGuiWindow(player));
            }
            return;
        } else if (arguments.length == 1) {
            Player target = Bukkit.getPlayer(arguments[0]);
            if (target == null) {
                commandSender.sendMessage(AdvancedPerks.getMessageConfiguration().getMessage("Command.Player-Offline", new ReplaceLogic("<name>", arguments[0])));
                return;
            }
            AdvancedPerks.getGuiManager().openGui(target, new PerkGuiWindow(target));
            return;
        }
        commandSender.sendMessage(AdvancedPerks.getMessageConfiguration().getMessage("Command.Open.Syntax"));
    }

    @Override
    public List<String> handleTabComplete(CommandSender commandSender, String... arguments) {
        return null;
    }
}
