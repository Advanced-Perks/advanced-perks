package de.fabilucius.advancedperks.commands.impl.subcommands;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commands.SubCommand;
import de.fabilucius.advancedperks.commons.ReplaceLogic;
import de.fabilucius.advancedperks.gui.PerkGuiWindow;
import de.fabilucius.advancedperks.utilities.MessageConfigReceiver;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@SubCommand.Details(identifier = "open", permission = "advancedperks.command.open", aliases = {"o"})
public class OpenSubCommand extends SubCommand {
    @Override
    public void handleCommand(CommandSender commandSender, String... arguments) {
        if (arguments.length == 0) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                AdvancedPerks.getInstance().getGuiManager().openGui(player, new PerkGuiWindow(player));
            }
            return;
        } else if (arguments.length == 1) {
            Player target = Bukkit.getPlayer(arguments[0]);
            if (target == null) {
                commandSender.sendMessage(MessageConfigReceiver.getMessageWithReplace("Command.Player-Offline", new ReplaceLogic("<name>", arguments[0])));
                return;
            }
            AdvancedPerks.getInstance().getGuiManager().openGui(target, new PerkGuiWindow(target));
            return;
        }
        commandSender.sendMessage(MessageConfigReceiver.getMessage("Command.Open.Syntax"));
    }

    @Override
    public List<String> handleTabComplete(CommandSender commandSender, String... arguments) {
        return null;
    }
}
