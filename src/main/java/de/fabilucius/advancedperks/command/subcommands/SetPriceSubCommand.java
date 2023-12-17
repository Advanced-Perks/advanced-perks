package de.fabilucius.advancedperks.command.subcommands;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import de.fabilucius.advancedperks.core.command.AbstractSubCommand;
import de.fabilucius.advancedperks.core.command.annotation.CommandIdentifier;
import de.fabilucius.advancedperks.core.command.annotation.Permission;
import de.fabilucius.advancedperks.core.configuration.ConfigurationLoader;
import de.fabilucius.advancedperks.core.configuration.exception.ConfigurationInitializationException;
import de.fabilucius.advancedperks.core.configuration.replace.ReplaceOptions;
import de.fabilucius.advancedperks.perk.Perk;
import de.fabilucius.advancedperks.registry.PerkRegistryImpl;
import de.fabilucius.advancedperks.registry.model.SetPriceResult;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;

import java.util.List;

@CommandIdentifier("setprice")
@Permission("advancedperks.command.setprice")
public class SetPriceSubCommand extends AbstractSubCommand {

    @Inject
    private PerkRegistryImpl perkRegistryImpl;

    @Inject
    protected SetPriceSubCommand(ConfigurationLoader configurationLoader) throws ConfigurationInitializationException {
        super(configurationLoader);
    }

    @Override
    public void executeCommand(CommandSender commandSender, String... arguments) {
        if (arguments.length == 2) {
            try {
                Double price = NumberUtils.createDouble(arguments[1]);
                Perk perk = this.perkRegistryImpl.getPerkByIdentifier(arguments[0]);
                if (perk != null) {
                    SetPriceResult setPriceResult = this.perkRegistryImpl.setPrice(perk, Double.valueOf(arguments[1]));
                    if (setPriceResult.equals(SetPriceResult.PRICE_SET)) {
                        commandSender.sendMessage(this.messagesConfiguration.getComputedString("command.perks.setprice.success",
                                new ReplaceOptions("<perk>", perk.getIdentifier()),
                                new ReplaceOptions("<price>", price.toString())));
                    } else {
                        commandSender.sendMessage(this.messagesConfiguration.getComputedString("command.perks.setprice.error"));
                    }
                    return;
                }
                commandSender.sendMessage(this.messagesConfiguration.getComputedString("command.perks.setprice.perk_doesnt_exist",
                        new ReplaceOptions("<perk>", arguments[0])));
            } catch (NumberFormatException exception) {
                commandSender.sendMessage(this.messagesConfiguration.getComputedString("command.perks.setprice.no_valid_price",
                        new ReplaceOptions("<price>", arguments[1])));
            }
        } else {
            commandSender.sendMessage(this.messagesConfiguration.getComputedString("command.perks.setprice.syntax"));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, String... arguments) {
        if (arguments.length == 1) {
            return this.perkRegistryImpl.getPerks().stream()
                    .map(Perk::getIdentifier)
                    .filter(identifier -> identifier.toLowerCase().startsWith(arguments[0].toLowerCase()))
                    .toList();
        }
        return arguments.length >= 3 ? Lists.newArrayList("") : null;
    }
}
