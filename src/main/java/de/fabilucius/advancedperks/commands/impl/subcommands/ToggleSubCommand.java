package de.fabilucius.advancedperks.commands.impl.subcommands;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commands.SubCommand;
import de.fabilucius.advancedperks.commons.ReplaceLogic;
import de.fabilucius.advancedperks.perks.Perk;
import de.fabilucius.advancedperks.utilities.MessageConfigReceiver;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SubCommand.Details(identifier = "toggle", aliases = {"toggle", "t"}, permission = "advancedperks.command.toggle")
public class ToggleSubCommand extends SubCommand {
    @Override
    public void handleCommand(CommandSender commandSender, String... arguments) {
        if (arguments.length == 2) {
            Player player = Bukkit.getPlayer(arguments[0]);
            if (player == null) {
                commandSender.sendMessage(MessageConfigReceiver.getMessageWithReplace("Command.Player-Offline", new ReplaceLogic("<name>", arguments[0])));
                return;
            }
            Perk perk = AdvancedPerks.getInstance().getPerkRegistry().getPerkByIdentifier(arguments[1]);
            if (perk == null) {
                commandSender.sendMessage(MessageConfigReceiver.getMessageWithReplace("Command.Toggle.Perk-Not-Found", new ReplaceLogic("<perk>", arguments[1])));
                return;
            }
            AdvancedPerks.getInstance().getPerkStateController().forceTogglePerk(player, perk);
            return;
        }
        commandSender.sendMessage(MessageConfigReceiver.getMessage("Command.Toggle.Syntax"));
    }

    @Override
    public List<String> handleTabComplete(CommandSender commandSender, String... arguments) {
        if (arguments.length == 2) {
            if (arguments[1].isEmpty()) {
                return AdvancedPerks.getInstance().getPerkRegistry().getPerks().stream()
                        .map(Perk::getIdentifier)
                        .collect(Collectors.toList());
            } else {
                return AdvancedPerks.getInstance().getPerkRegistry().getPerks().stream()
                        .map(Perk::getIdentifier)
                        .filter(identifier -> identifier.toLowerCase().startsWith(arguments[1].toLowerCase()))
                        .collect(Collectors.toList());
            }
        }
        return arguments.length > 1 ? Collections.emptyList() : null;
    }
}
