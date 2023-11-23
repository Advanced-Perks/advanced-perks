package de.fabilucius.advancedperks.command.subcommands;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import de.fabilucius.advancedperks.configuration.ConfigurationProvider;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationInitializationException;
import de.fabilucius.advancedperks.configuration.replace.ReplaceOptions;
import de.fabilucius.advancedperks.core.command.AbstractSubCommand;
import de.fabilucius.advancedperks.core.command.annotation.CommandIdentifier;
import de.fabilucius.advancedperks.core.command.annotation.Permission;
import de.fabilucius.advancedperks.data.state.PerkStateController;
import de.fabilucius.advancedperks.perk.Perk;
import de.fabilucius.advancedperks.registry.PerkRegistry;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@CommandIdentifier("disable")
@Permission("advancedperks.command.disable")
public class DisableSubCommand extends AbstractSubCommand {

    @Inject
    private PerkRegistry perkRegistry;

    @Inject
    private PerkStateController perkStateController;

    @Inject
    public DisableSubCommand(ConfigurationProvider configurationProvider) throws ConfigurationInitializationException {
        super(configurationProvider);
    }

    /* /perks disable <perk> [player] */
    @Override
    public void executeCommand(CommandSender commandSender, String... arguments) {
        if (arguments.length == 1) {
            if (commandSender instanceof Player player) {
                Perk perk = this.perkRegistry.getPerkByIdentifier(arguments[0]);
                if (perk != null) {
                    this.disablePerk(player, perk);
                    player.sendMessage(this.messagesConfiguration.getComputedString("command.perks.disable.success",
                            new ReplaceOptions("<perk>", perk.getIdentifier()),
                            new ReplaceOptions("<player>", player.getName())));
                    return;
                }
                player.sendMessage(this.messagesConfiguration.getComputedString("command.perks.disable.perk_doesnt_exist",
                        new ReplaceOptions("<perk>", arguments[0])));
                return;
            } else {
                commandSender.sendMessage("Cannot disable a perk for the console please specify a player /perk disable <perk> <player>.");
                return;
            }
        } else if (arguments.length == 2) {
            Perk perk = this.perkRegistry.getPerkByIdentifier(arguments[0]);
            if (perk == null) {
                commandSender.sendMessage(this.messagesConfiguration.getComputedString("command.perks.disable.perk_doesnt_exist",
                        new ReplaceOptions("<perk>", arguments[0])));
                return;
            }
            Player target = Bukkit.getPlayer(arguments[1]);
            if (target == null) {
                commandSender.sendMessage(this.messagesConfiguration.getComputedString("command.perks.disable.player_not_online",
                        new ReplaceOptions("<player>", arguments[1])));
                return;
            }
            this.disablePerk(target, perk);
            commandSender.sendMessage(this.messagesConfiguration.getComputedString("command.perks.disable.success",
                    new ReplaceOptions("<perk>", perk.getIdentifier()),
                    new ReplaceOptions("<player>", target.getName())));
            return;
        } else {
            commandSender.sendMessage(this.messagesConfiguration.getComputedString("command.perks.disable.syntax"));
            return;
        }
    }

    private void disablePerk(Player player, Perk perk) {
        this.perkStateController.disablePerk(player, perk);
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, String... arguments) {
        if (arguments.length == 1) {
            return this.perkRegistry.getPerks().stream()
                    .map(Perk::getIdentifier)
                    .filter(identifier -> identifier.toLowerCase().startsWith(arguments[0].toLowerCase()))
                    .toList();
        }
        return arguments.length >= 3 ? Lists.newArrayList("") : null;
    }
}
