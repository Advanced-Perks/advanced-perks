package de.fabilucius.advancedperks.commands.subcommands;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.command.command.AbstractSubCommand;
import de.fabilucius.advancedperks.commons.command.metadata.Permission;
import de.fabilucius.advancedperks.commons.configuration.utilities.ReplaceLogic;
import de.fabilucius.advancedperks.data.PerkData;
import org.apache.logging.log4j.util.Strings;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@Permission("advancedperks.command.info")
public class InfoSubCommand extends AbstractSubCommand {
    public InfoSubCommand() {
        super("info");
    }

    @Override
    public void handleCommandExecute(CommandSender commandSender, String... arguments) {
        if (arguments.length != 1) {
            commandSender.sendMessage(this.getMessageConfig().getMessage("Command.Info.Syntax"));
            return;
        }
        Player target = Bukkit.getPlayer(arguments[0]);
        if (target == null) {
            commandSender.sendMessage(this.getMessageConfig().getMessage("Command.Info.Player-Offline",
                    new ReplaceLogic("<player>", arguments[0])));
        } else {
            PerkData perkData = AdvancedPerks.getInstance().getPerkDataRepository().getPerkData(target);
            String boughtPerks = String.join(", ", perkData.getUnlockedPerks());
            this.getMessageConfig().getMessageList("Command.Info.Text",
                            new ReplaceLogic("<player>", target.getName()),
                            new ReplaceLogic("<bought_perks>", Strings.isEmpty(boughtPerks) ? "none" : boughtPerks))
                    .forEach(commandSender::sendMessage);
        }
    }

    @Override
    public List<String> handleTabComplete(CommandSender commandSender, String... arguments) {
        return null;
    }
}
