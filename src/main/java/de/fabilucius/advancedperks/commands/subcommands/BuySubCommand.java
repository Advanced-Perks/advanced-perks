package de.fabilucius.advancedperks.commands.subcommands;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.economy.PurchaseResult;
import de.fabilucius.advancedperks.perks.Perk;
import de.fabilucius.advancedperks.settings.MessageConfiguration;
import de.fabilucius.advancedperks.commons.command.command.AbstractSubCommand;
import de.fabilucius.advancedperks.commons.configuration.utilities.ReplaceLogic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class BuySubCommand extends AbstractSubCommand {

    public BuySubCommand() {
        super("buy");
    }

    @Override
    public void handleCommandExecute(CommandSender commandSender, String... strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            MessageConfiguration configuration = AdvancedPerks.getMessageConfiguration();
            if (!AdvancedPerks.getEconomyController().isPresent()) {
                player.sendMessage(configuration.getMessage("Command.Buy.No-Eco-Support"));
                return;
            }
            if (strings.length == 1) {
                Perk perk = AdvancedPerks.getPerkRegistry().getPerkByIdentifier(strings[0]);
                if (perk == null) {
                    player.sendMessage(configuration.getMessage("Command.Buy.Perk-Not-Found",
                            new ReplaceLogic("<perk>", strings[0])));
                    return;
                }
                PurchaseResult result = AdvancedPerks.getEconomyController().get().buyPerk(player, perk);
                switch (result) {
                    case SUCCESS:
                        player.sendMessage(configuration.getMessage("Command.Buy.Success",
                                new ReplaceLogic("<perk>", perk.getIdentifier())));
                        break;
                    case ALREADY_OWNS_PERK:
                        player.sendMessage(configuration.getMessage("Command.Buy.Already-Unlocked"));
                        break;
                    case NOT_ENOUGH_FUNDS:
                        player.sendMessage(configuration.getMessage("Command.Buy.Not-Enough-Money"));
                        break;
                    case NO_VALID_PRICE:
                        player.sendMessage(configuration.getMessage("Command.Buy.Cannot-Be-Purchased"));
                        break;
                    case ERROR:
                        player.sendMessage(configuration.getMessage("Command.Buy.Error",
                                new ReplaceLogic("<perk>", strings[0])));
                        break;
                }
            }
        }
    }

    @Override
    public List<String> handleTabComplete(CommandSender commandSender, String... strings) {
        return AdvancedPerks.getPerkRegistry().getPerks().stream()
                .map(Perk::getIdentifier)
                .filter(perk -> {
                    if (strings.length == 0 || strings[0].isEmpty()) {
                        return true;
                    } else {
                        return perk.toLowerCase().startsWith(strings[0].toLowerCase());
                    }
                })
                .collect(Collectors.toList());
    }
}
