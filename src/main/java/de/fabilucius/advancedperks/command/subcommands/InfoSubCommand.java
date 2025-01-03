package de.fabilucius.advancedperks.command.subcommands;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.core.configuration.replace.ReplaceOptions;
import de.fabilucius.advancedperks.core.command.AbstractSubCommand;
import de.fabilucius.advancedperks.core.command.annotation.CommandIdentifier;
import de.fabilucius.advancedperks.core.command.annotation.Permission;
import de.fabilucius.advancedperks.core.configuration.type.MessageConfiguration;
import de.fabilucius.advancedperks.core.mojang.MojangProfileData;
import de.fabilucius.advancedperks.core.mojang.MojangProfileFetcher;
import de.fabilucius.advancedperks.data.PerkDataRepository;
import de.fabilucius.advancedperks.data.UnloadedPerkData;
import de.fabilucius.advancedperks.perk.Perk;
import org.apache.commons.codec.digest.DigestUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@CommandIdentifier("info")
@Permission("advancedperks.command.info")
public class InfoSubCommand extends AbstractSubCommand {

    @Inject
    private PerkDataRepository perkDataRepository;

    @Inject
    private MessageConfiguration messageConfiguration;

    @Inject
    public InfoSubCommand() {
        super();
    }

    /* /perks info <player> */
    @SuppressWarnings("deprecation")
    @Override
    public void executeCommand(CommandSender commandSender, String... arguments) {
        /* Info about the sender himself */
        if (arguments.length == 0) {
            if (commandSender instanceof Player player) {
                this.printPerkInfo(player, player.getUniqueId(), player.getName());
            } else {
                commandSender.sendMessage("You cannot have perk data to check another player do /perk info <player>.");
            }
        } else {
            Player target = Bukkit.getPlayer(arguments[0]);
            if (target != null) {
                this.printPerkInfo(commandSender, target.getUniqueId(), target.getName());
                return;
            }
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(arguments[0]);
            if (offlinePlayer.hasPlayedBefore()) {
                this.printPerkInfo(commandSender, offlinePlayer.getUniqueId(), offlinePlayer.getName());
                return;
            }
            MojangProfileData mojangProfileData = MojangProfileFetcher.fetchPlayerProfile(arguments[0]);
            if (mojangProfileData != null) {
                this.printPerkInfo(commandSender, mojangProfileData.uniqueId(), mojangProfileData.name());
                return;
            }
            commandSender.sendMessage(this.messageConfiguration.getMessage("command.perks.info.player_not_found",
                    new ReplaceOptions("<player>", arguments[0])));
        }
    }

    private void printPerkInfo(CommandSender messageReceiver, UUID infoAbout, String name) {
        this.perkDataRepository.getPerkDataByUuid(infoAbout).thenAcceptAsync(perkData -> {
            if (perkData instanceof UnloadedPerkData) {
                messageReceiver.sendMessage(this.messageConfiguration.getMessage("command.perks.info.still_loading",
                        new ReplaceOptions("<player>", name)));
            } else {
                String enabledPerks = perkData.getEnabledPerks().stream().map(Perk::getIdentifier).collect(Collectors.joining(" "));
                String boughtPerks = String.join(" ", perkData.getBoughtPerks());
                this.messageConfiguration.getMessageList("command.perks.info.text",
                                new ReplaceOptions("<player>", name),
                                new ReplaceOptions("<enabled_perks>", enabledPerks.isEmpty() ? "-" : enabledPerks),
                                new ReplaceOptions("<bought_perks>", boughtPerks.isEmpty() ? "-" : boughtPerks),
                                new ReplaceOptions("<data_hash>", DigestUtils.md5Hex(perkData.calculateDataHash())))
                        .forEach(messageReceiver::sendMessage);
            }
        });
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, String... arguments) {
        return null;
    }
}
