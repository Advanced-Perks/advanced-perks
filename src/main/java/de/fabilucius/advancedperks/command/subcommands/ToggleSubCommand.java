package de.fabilucius.advancedperks.command.subcommands;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import de.fabilucius.advancedperks.core.configuration.ConfigurationLoader;
import de.fabilucius.advancedperks.core.configuration.exception.ConfigurationInitializationException;
import de.fabilucius.advancedperks.core.configuration.replace.ReplaceOptions;
import de.fabilucius.advancedperks.core.command.AbstractSubCommand;
import de.fabilucius.advancedperks.core.command.annotation.CommandIdentifier;
import de.fabilucius.advancedperks.core.command.annotation.Permission;
import de.fabilucius.advancedperks.data.state.PerkStateController;
import de.fabilucius.advancedperks.perk.Perk;
import de.fabilucius.advancedperks.registry.PerkRegistryImpl;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@CommandIdentifier("toggle")
@Permission("advancedperks.command.toggle")
public class ToggleSubCommand extends AbstractSubCommand {

    @Inject
    private PerkRegistryImpl perkRegistryImpl;

    @Inject
    private PerkStateController perkStateController;

    @Inject
    public ToggleSubCommand(ConfigurationLoader configurationLoader) throws ConfigurationInitializationException {
        super(configurationLoader);
    }

    /* /perks toggle <perk> [player] */
    @Override
    public void executeCommand(CommandSender commandSender, String... arguments) {
        if (arguments.length == 1) {
            if (commandSender instanceof Player player) {
                Perk perk = this.perkRegistryImpl.getPerkByIdentifier(arguments[0]);
                if (perk != null) {
                    this.forceTogglePerk(player, perk);
                    player.sendMessage(this.messagesConfiguration.getComputedString("command.perks.toggle.success",
                            new ReplaceOptions("<perk>", perk.getIdentifier()),
                            new ReplaceOptions("<player>", player.getName())));
                    return;
                }
                player.sendMessage(this.messagesConfiguration.getComputedString("command.perks.toggle.perk_doesnt_exist",
                        new ReplaceOptions("<perk>", arguments[0])));
            } else {
                commandSender.sendMessage("Cannot enable a perk for the console please specify a player /perk toggle <perk> <player>.");
            }
        } else if (arguments.length == 2) {
            Perk perk = this.perkRegistryImpl.getPerkByIdentifier(arguments[0]);
            if (perk == null) {
                commandSender.sendMessage(this.messagesConfiguration.getComputedString("command.perks.toggle.perk_doesnt_exist",
                        new ReplaceOptions("<perk>", arguments[0])));
                return;
            }
            Player target = Bukkit.getPlayer(arguments[1]);
            if (target == null) {
                commandSender.sendMessage(this.messagesConfiguration.getComputedString("command.perks.toggle.player_not_online",
                        new ReplaceOptions("<player>", arguments[1])));
                return;
            }
            this.forceTogglePerk(target, perk);
            commandSender.sendMessage(this.messagesConfiguration.getComputedString("command.perks.toggle.success",
                    new ReplaceOptions("<perk>", perk.getIdentifier()),
                    new ReplaceOptions("<player>", target.getName())));
        } else {
            commandSender.sendMessage(this.messagesConfiguration.getComputedString("command.perks.toggle.syntax"));
        }
    }

    private void forceTogglePerk(Player player, Perk perk) {
        this.perkStateController.forceTogglePerk(player, perk);
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
