package de.fabilucius.advancedperks.perks;

import com.google.common.collect.Lists;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.ReplaceLogic;
import de.fabilucius.advancedperks.commons.sql.SqlType;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.perks.tasks.SavePerkDataTask;
import de.fabilucius.advancedperks.settings.SettingsConfiguration;
import de.fabilucius.advancedperks.utilities.MessageConfigReceiver;
import de.fabilucius.sympel.database.AbstractDatabase;
import de.fabilucius.sympel.database.details.Credentials;
import de.fabilucius.sympel.database.types.FileDatabase;
import de.fabilucius.sympel.database.types.RemoteDatabase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PerkStateController {

    private static final Logger LOGGER = Bukkit.getLogger();

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final AbstractDatabase abstractDatabase;
    private final int globalMaxPerks;

    public PerkStateController() {
        SettingsConfiguration configuration = AdvancedPerks.getInstance().getSettingsConfiguration();
        if (configuration.getSqlType().equals(SqlType.DATABASE)) {
            Credentials credentials = Credentials.withAuth(configuration.getSqlUserName(), configuration.getSqlPassword());
            this.abstractDatabase = RemoteDatabase.withCredentials(configuration.getSqlUrl(), credentials);
            LOGGER.log(Level.INFO, "Successfully connected to the database.");
        } else {
            File databaseFile = new File(AdvancedPerks.getInstance().getDataFolder(), "data.db");
            if (!databaseFile.exists()) {
                try {
                    databaseFile.getParentFile().mkdirs();
                    databaseFile.createNewFile();
                } catch (Exception exception) {
                    LOGGER.log(Level.SEVERE, "An error occurred while " +
                            "creating a local database file for perk data storage:", exception);
                }
            }
            this.abstractDatabase = FileDatabase.fromFile(databaseFile);
            LOGGER.log(Level.INFO, "Successfully connected to the local file based database.");
        }
        this.getAbstractDatabase().customUpdate("CREATE TABLE IF NOT EXISTS activated_perks(UUID varchar(36) PRIMARY KEY,PERKS varchar(999))");
        this.globalMaxPerks = AdvancedPerks.getInstance().getSettingsConfiguration().getGlobalMaxPerks();
    }

    public void disableAllPerks(Player player) {
        PerkData perkData = AdvancedPerks.getInstance().getPerkDataRepository().getPerkData(player);
        Lists.newArrayList(perkData.getActivatedPerks()).forEach(perk -> this.disablePerk(player, perk));
    }

    public void forceTogglePerk(Player player, Perk perk) {
        PerkData perkData = AdvancedPerks.getInstance().getPerkDataRepository().getPerkData(player);
        if (perkData.isPerkActivated(perk)) {
            this.disablePerk(player, perk);
        } else {
            this.forceEnablePerk(player, perk);
        }
    }

    public void togglePerk(Player player, Perk perk) {
        PerkData perkData = AdvancedPerks.getInstance().getPerkDataRepository().getPerkData(player);
        if (perkData.isPerkActivated(perk)) {
            this.disablePerk(player, perk);
        } else {
            this.enablePerk(player, perk);
        }
    }

    public void enablePerk(Player player, Perk perk) {
        PerkData perkData = AdvancedPerks.getInstance().getPerkDataRepository().getPerkData(player);

        /* max perk at once checking */
        int maxAmountOfPerks = Math.max(this.getGlobalMaxPerks(), perkData.getMaxPerks());
        if (this.getGlobalMaxPerks() != -1 && perkData.getAmountOfActivatedPerks() >= maxAmountOfPerks) {
            perkData.refreshMaxPerks();
            if (perkData.getAmountOfActivatedPerks() >= perkData.getMaxPerks()) {
                player.sendMessage(MessageConfigReceiver.getMessageWithReplace("Perks.Too-Many-Perks-Enabled",
                        new ReplaceLogic("<amount>", String.valueOf(maxAmountOfPerks))));
                return;
            }
        }

        /* perk permission checking */
        if (!perk.getPermission().isEmpty() && !player.hasPermission(perk.getPermission())) {
            player.sendMessage(MessageConfigReceiver.getMessage("Perks.No-Permission"));
            return;
        }

        /* perk world checking */
        if (perk.getDisabledWorlds().contains(player.getWorld().getName())) {
            player.sendMessage(MessageConfigReceiver.getMessageWithReplace("Perks.Disabled-By-World",
                    new ReplaceLogic("<perk_name>", perk.getIdentifier()),
                    new ReplaceLogic("<world_name>", player.getWorld().getName())));
            return;
        }

        if (!perkData.isPerkActivated(perk)) {
            perkData.getActivatedPerks().add(perk);
            perk.prePerkEnable(player);
        }
    }

    public void disablePerk(Player player, Perk perk) {
        PerkData perkData = AdvancedPerks.getInstance().getPerkDataRepository().getPerkData(player);
        if (perkData.isPerkActivated(perk)) {
            perkData.getActivatedPerks().remove(perk);
            perk.prePerkDisable(player);
        }
    }

    public void forceEnablePerk(Player player, Perk perk) {
        PerkData perkData = AdvancedPerks.getInstance().getPerkDataRepository().getPerkData(player);
        if (!perkData.isPerkActivated(perk)) {
            perkData.getActivatedPerks().add(perk);
            perk.prePerkEnable(player);
        }
    }

    public void loadPerkData(PerkData perkData) {
        this.getExecutorService().submit(() -> {
            String uuid = perkData.getPlayer().getUniqueId().toString();
            ResultSet resultSet = this.getAbstractDatabase().selectQuery("activated_perks", Collections.singletonList("PERKS"), "UUID = '" + uuid + "'");
            Bukkit.getScheduler().runTask(AdvancedPerks.getInstance(), () -> {
                try {
                    while (resultSet.next()) {
                        String perkString = resultSet.getString("PERKS");
                        Arrays.stream(perkString.split(",")).forEach(line -> {
                            Perk perk = AdvancedPerks.getInstance().getPerkRegistry().getPerkByIdentifier(line);
                            if (perk != null) {
                                this.enablePerk(perkData.getPlayer(), perk);
                            }
                        });
                    }
                } catch (Exception ignored) {
                }
            });
        });
    }

    public void savePerkData(PerkData perkData) {
        this.getExecutorService().submit(new SavePerkDataTask(perkData, this.getAbstractDatabase()));
    }

    public void handleShutdown() {
        List<SavePerkDataTask> savePerkDataTasks = AdvancedPerks.getInstance().getPerkDataRepository().getPerkDataCache().values().stream()
                .map(perkData -> new SavePerkDataTask(perkData, this.getAbstractDatabase()))
                .collect(Collectors.toList());
        try {
            this.getExecutorService().invokeAll(savePerkDataTasks);
        } catch (InterruptedException interruptedException) {
            LOGGER.log(Level.SEVERE, "There was an error while shutting down the PerkStateController:", interruptedException);
        }finally {
            this.getAbstractDatabase().closeConnection();
        }
    }

    /* the getter and setter of this class */

    public int getGlobalMaxPerks() {
        return globalMaxPerks;
    }

    public AbstractDatabase getAbstractDatabase() {
        return abstractDatabase;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
