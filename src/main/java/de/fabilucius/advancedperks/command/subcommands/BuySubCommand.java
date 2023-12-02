package de.fabilucius.advancedperks.command.subcommands;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import de.fabilucius.advancedperks.configuration.ConfigurationLoader;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationInitializationException;
import de.fabilucius.advancedperks.configuration.replace.ReplaceOptions;
import de.fabilucius.advancedperks.core.command.AbstractSubCommand;
import de.fabilucius.advancedperks.core.command.annotation.CommandIdentifier;
import de.fabilucius.advancedperks.core.command.annotation.Permission;
import de.fabilucius.advancedperks.core.economy.EconomyController;
import de.fabilucius.advancedperks.core.economy.PerkBuyResult;
import de.fabilucius.advancedperks.perk.Perk;
import de.fabilucius.advancedperks.registry.PerkRegistryImpl;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@CommandIdentifier("buy")
@Permission("advancedperks.command.buy")
public class BuySubCommand extends AbstractSubCommand {

    @Inject
    private PerkRegistryImpl perkRegistry;

    @Inject
    private EconomyController economyController;

    @Inject
    public BuySubCommand(ConfigurationLoader configurationLoader) throws ConfigurationInitializationException {
        super(configurationLoader);
    }


    /* /perks buy <perk> */
    @Override
    public void executeCommand(CommandSender commandSender, String... arguments) {
        if (commandSender instanceof Player player) {
            if (arguments.length == 1) {
                Perk perk = this.perkRegistry.getPerkByIdentifier(arguments[0]);
                if (perk == null) {
                    player.sendMessage(this.messagesConfiguration.getComputedString("command.perks.buy.perk_not_found",
                            new ReplaceOptions("<perk>", arguments[0])));
                    return;
                }
                PerkBuyResult result = this.economyController.buyPerk(player, perk);
                switch (result) {
                    case ERROR ->
                            player.sendMessage(this.messagesConfiguration.getComputedString("command.perks.buy.error"));
                    case ALREADY_BOUGHT_PERK ->
                            player.sendMessage(this.messagesConfiguration.getComputedString("command.perks.buy.already_bought",
                                    new ReplaceOptions("<perk>", perk.getIdentifier())));
                    case NO_PRICE_SET ->
                            player.sendMessage(this.messagesConfiguration.getComputedString("command.perks.buy.no_price_set",
                                    new ReplaceOptions("<perk>", perk.getIdentifier())));
                    case NOT_ENOUGH_FUNDS ->
                            player.sendMessage(this.messagesConfiguration.getComputedString("command.perks.buy.not_enough_money",
                                    new ReplaceOptions("<perk>", perk.getIdentifier())));
                    case NO_ECONOMY_INTERFACE ->
                            player.sendMessage(this.messagesConfiguration.getComputedString("command.perks.buy.not_set_up"));
                    default ->
                            player.sendMessage(this.messagesConfiguration.getComputedString("command.perks.buy.success",
                                    new ReplaceOptions("<perk>", perk.getIdentifier())));
                }
            } else {
                player.sendMessage(this.messagesConfiguration.getComputedString("command.perks.buy.syntax"));
            }
        } else {
            commandSender.sendMessage("This command is only available for players.");
        }
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, String... arguments) {
        if (arguments.length == 1) {
            return this.perkRegistry.getPerks().stream()
                    .map(Perk::getIdentifier)
                    .filter(identifier -> identifier.toLowerCase().startsWith(arguments[0].toLowerCase()))
                    .toList();
        }
        return Lists.newArrayList();
    }
}
