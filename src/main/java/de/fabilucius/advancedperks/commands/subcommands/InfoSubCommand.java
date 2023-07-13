package de.fabilucius.advancedperks.commands.subcommands;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.command.command.AbstractSubCommand;
import de.fabilucius.advancedperks.commons.command.metadata.Permission;
import de.fabilucius.advancedperks.commons.configuration.utilities.ReplaceLogic;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.data.PerkDataFetcher;
import de.fabilucius.advancedperks.perks.Perk;
import de.fabilucius.advancedperks.utilities.PlayerUniqueIdFetcher;
import org.apache.logging.log4j.util.Strings;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Permission("advancedperks.command.info")
public class InfoSubCommand extends AbstractSubCommand {
    public InfoSubCommand() {
        super("info");
    }

    //TODO cleanup code and make it more maintainable
    @Override
    public void handleCommandExecute(CommandSender commandSender, String... arguments) {
        if (arguments.length != 1) {
            commandSender.sendMessage(this.getMessageConfig().getMessage("Command.Info.Syntax"));
            return;
        }
        Player target = Bukkit.getPlayer(arguments[0]);
        if (target == null) {
            Bukkit.getScheduler().runTaskAsynchronously(AdvancedPerks.getInstance(), () -> {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(arguments[0]);
                UUID uuidToCheck;
                String name;
                if (offlinePlayer.hasPlayedBefore()) {
                    uuidToCheck = offlinePlayer.getUniqueId();
                    name = offlinePlayer.getName();
                } else {
                    uuidToCheck = PlayerUniqueIdFetcher.getUUID(arguments[0]);
                    name = PlayerUniqueIdFetcher.getName(uuidToCheck);
                }
                if (uuidToCheck == null) {
                    commandSender.sendMessage(this.getMessageConfig().getMessage("Command.Info.Player-Does-Not-Exist",
                            new ReplaceLogic("<player>", arguments[0])));
                } else {
                    CompletableFuture<List<Perk>> enabledPerkFetchFuture = PerkDataFetcher.fetchEnabledPerksByUuid(uuidToCheck);
                    CompletableFuture<List<String>> boughtPerkFetchFuture = PerkDataFetcher.fetchBoughtPerksByUuid(uuidToCheck);
                    CompletableFuture<Void> dataFuture = CompletableFuture.allOf(enabledPerkFetchFuture, boughtPerkFetchFuture);
                    try {
                        dataFuture.get();
                        String boughtPerks = String.join(", ", boughtPerkFetchFuture.get());
                        String enabledPerks = String.join(", ", enabledPerkFetchFuture.get().stream().map(Perk::getIdentifier).toList());
                        this.getMessageConfig().getMessageList("Command.Info.Text",
                                        new ReplaceLogic("<player>", name),
                                        new ReplaceLogic("<bought_perks>", Strings.isEmpty(boughtPerks) ? "none" : boughtPerks),
                                        new ReplaceLogic("<enabled_perks>", Strings.isEmpty(enabledPerks) ? "none" : enabledPerks))
                                .forEach(commandSender::sendMessage);
                    } catch (InterruptedException | ExecutionException e) {
                        commandSender.sendMessage("§cAn error occurred while executing the command please " +
                                "report the exception from the console to the author.");
                    }
                }
            });
        } else {
            PerkData perkData = AdvancedPerks.getInstance().getPerkDataRepository().getPerkData(target);
            String boughtPerks = String.join(", ", perkData.getUnlockedPerks());
            String enabledPerks = String.join(", ", perkData.getActivatedPerks().stream().map(Perk::getIdentifier).toList());
            this.getMessageConfig().getMessageList("Command.Info.Text",
                            new ReplaceLogic("<player>", target.getName()),
                            new ReplaceLogic("<bought_perks>", Strings.isEmpty(boughtPerks) ? "none" : boughtPerks),
                            new ReplaceLogic("<enabled_perks>", Strings.isEmpty(enabledPerks) ? "none" : enabledPerks))
                    .forEach(commandSender::sendMessage);
        }
    }

    @Override
    public List<String> handleTabComplete(CommandSender commandSender, String... arguments) {
        return null;
    }
}
