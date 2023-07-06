package de.fabilucius.advancedperks.commands.subcommands;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.gui.PerkGuiWindow;
import de.fabilucius.advancedperks.commons.command.command.AbstractSubCommand;
import de.fabilucius.advancedperks.commons.command.metadata.Permission;
import de.fabilucius.advancedperks.commons.configuration.utilities.ReplaceLogic;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@Permission("advancedperks.command.open")
public class OpenSubCommand extends AbstractSubCommand {
    public OpenSubCommand() {
        super("open");
    }

    @Override
    public void handleCommandExecute(CommandSender commandSender, String... arguments) {
        if (arguments.length == 0) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                AdvancedPerks.getInstance().getGuiManager().openGui(player, new PerkGuiWindow(player));
            }
            return;
        } else if (arguments.length == 1) {
            Player target = Bukkit.getPlayer(arguments[0]);
            if (target == null) {
                commandSender.sendMessage(AdvancedPerks.getInstance().getMessageConfiguration().getMessage("Command.Player-Offline", new ReplaceLogic("<name>", arguments[0])));
                return;
            }
            AdvancedPerks.getInstance().getGuiManager().openGui(target, new PerkGuiWindow(target));
            return;
        }
        commandSender.sendMessage(AdvancedPerks.getInstance().getMessageConfiguration().getMessage("Command.Open.Syntax"));
    }

    @Override
    public List<String> handleTabComplete(CommandSender commandSender, String... arguments) {
        return null;
    }
}
