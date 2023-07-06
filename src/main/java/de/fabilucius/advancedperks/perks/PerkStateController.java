package de.fabilucius.advancedperks.perks;

import com.google.common.collect.Lists;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.database.AbstractDatabase;
import de.fabilucius.advancedperks.commons.database.details.Credentials;
import de.fabilucius.advancedperks.commons.database.types.FileDatabase;
import de.fabilucius.advancedperks.commons.database.types.RemoteDatabase;
import de.fabilucius.advancedperks.commons.sql.SqlType;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.perks.tasks.SavePerkDataTask;
import de.fabilucius.advancedperks.settings.SettingsConfiguration;
import de.fabilucius.advancedperks.commons.configuration.utilities.ReplaceLogic;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PerkStateController {

    private static final Logger LOGGER = AdvancedPerks.getInstance().getLogger();

    private final AbstractDatabase abstractDatabase;
    private final int globalMaxPerks;

    private PerkStateController(AdvancedPerks plugin) {
        SettingsConfiguration configuration = plugin.getSettingsConfiguration();
        if (configuration.SQL_TYPE.equals(SqlType.DATABASE)) {
            Credentials credentials = Credentials.withAuth(configuration.SQL_USERNAME.get(), configuration.SQL_PASSWORD.get());
            this.abstractDatabase = RemoteDatabase.withCredentials(configuration.SQL_URL.get(), credentials);
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

        this.getAbstractDatabase().customUpdate(
                "CREATE TABLE IF NOT EXISTS `unlocked_perks` " +
                        "(" +
                        "`uuid` varchar(36)," +
                        "`perk` varchar(128)" +
                        ")"
        );
        this.getAbstractDatabase().customUpdate(
                "CREATE TABLE IF NOT EXISTS `enabled_perks` " +
                        "(" +
                        "`uuid` varchar(36)," +
                        "`perk` varchar(128)" +
                        ")"
        );
        this.globalMaxPerks = plugin.getSettingsConfiguration().GLOBAL_MAX_PERKS.get();
    }

    public void disableAllPerks(Player player) {
        AdvancedPerks.getInstance().getPerkDataRepository().consumePerkData(player, perkData -> {
            /* Boxing it with newArrayList prevents a concurrent modification exception TODO: Fix the bad design here */
            Lists.newArrayList(perkData.getActivatedPerks()).forEach(perk -> this.disablePerk(player, perk));
        });
    }

    public void forceTogglePerk(Player player, Perk perk) {
        AdvancedPerks.getInstance().getPerkDataRepository().consumePerkData(player, perkData -> {
            if (perkData.isPerkActivated(perk)) {
                this.disablePerk(player, perk);
            } else {
                this.forceEnablePerk(player, perk);
            }
        });
    }

    public void togglePerk(Player player, Perk perk) {
        AdvancedPerks.getInstance().getPerkDataRepository().consumePerkData(player, perkData -> {
            if (perkData.isPerkActivated(perk)) {
                this.disablePerk(player, perk);
            } else {
                this.enablePerk(player, perk);
            }
        });
    }

    public void enablePerk(Player player, Perk perk) {
        AdvancedPerks.getInstance().getPerkDataRepository().consumePerkData(player, perkData -> {
            /* max perk at once checking */
            int maxAmountOfPerks = Math.max(this.getGlobalMaxPerks(), perkData.getMaxPerks());
            if (this.getGlobalMaxPerks() != -1 && perkData.getAmountOfActivatedPerks() >= maxAmountOfPerks) {
                perkData.refreshMaxPerks();
                if (perkData.getAmountOfActivatedPerks() >= perkData.getMaxPerks()) {
                    player.sendMessage(AdvancedPerks.getInstance().getMessageConfiguration().getMessage("Perks.Too-Many-Perks-Enabled",
                            new ReplaceLogic("<amount>", String.valueOf(maxAmountOfPerks))));
                    return;
                }
            }

            /* perk permission checking */
            if ((!perk.getPermission().isEmpty() && !player.hasPermission(perk.getPermission())) &&
                    !perkData.getUnlockedPerks().contains(perk.getIdentifier())) {
                player.sendMessage(AdvancedPerks.getInstance().getMessageConfiguration().getMessage("Perks.No-Permission"));
                return;
            }

            /* perk world checking */
            if (perk.getDisabledWorlds().contains(player.getWorld().getName())) {
                player.sendMessage(AdvancedPerks.getInstance().getMessageConfiguration().getMessage("Perks.Disabled-By-World",
                        new ReplaceLogic("<perk_name>", perk.getIdentifier()),
                        new ReplaceLogic("<world_name>", player.getWorld().getName())));
                return;
            }

            if (!perkData.isPerkActivated(perk)) {
                perkData.getActivatedPerks().add(perk);
                perk.prePerkEnable(player);
                perkData.getPerkDataStatus().setDataChanged();
            }
        });
    }

    public void disablePerk(Player player, Perk perk) {
        AdvancedPerks.getInstance().getPerkDataRepository().consumePerkData(player, perkData -> {
            if (perkData.isPerkActivated(perk)) {
                perkData.getActivatedPerks().remove(perk);
                perk.prePerkDisable(player);
                perkData.getPerkDataStatus().setDataChanged();
            }
        });
    }

    public void forceEnablePerk(Player player, Perk perk) {
        AdvancedPerks.getInstance().getPerkDataRepository().consumePerkData(player, perkData -> {
            if (!perkData.isPerkActivated(perk)) {
                perkData.getActivatedPerks().add(perk);
                perk.prePerkEnable(player);
                perkData.getPerkDataStatus().setDataChanged();
            }
        });
    }

    public void savePerkData(PerkData perkData) {
        if (perkData.getPerkDataStatus().isDataChanged()) {
            Bukkit.getScheduler().runTaskAsynchronously(AdvancedPerks.getInstance(), new SavePerkDataTask(perkData, this.getAbstractDatabase()));
        }
    }

    public void handleShutdown() {
        List<SavePerkDataTask> savePerkDataTasks = AdvancedPerks.getInstance().getPerkDataRepository().getPerkDataCache().values().stream()
                .map(perkData -> new SavePerkDataTask(perkData, this.getAbstractDatabase()))
                .collect(Collectors.toList());
        savePerkDataTasks.forEach(SavePerkDataTask::run);
        this.getAbstractDatabase().closeConnection();
    }

    /* the getter and setter of this class */

    public int getGlobalMaxPerks() {
        return globalMaxPerks;
    }

    public AbstractDatabase getAbstractDatabase() {
        return abstractDatabase;
    }

    /*Singleton stuff */

    private static PerkStateController instance;

    public static PerkStateController getSingleton(AdvancedPerks plugin) {
        if (instance == null) {
            instance = new PerkStateController(plugin);
        }
        return instance;
    }

}
