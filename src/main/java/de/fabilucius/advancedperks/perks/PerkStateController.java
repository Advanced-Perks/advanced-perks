package de.fabilucius.advancedperks.perks;

import com.google.common.collect.Lists;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.ReplaceLogic;
import de.fabilucius.advancedperks.commons.sql.SqlConnection;
import de.fabilucius.advancedperks.commons.sql.SqlType;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.settings.SettingsConfiguration;
import de.fabilucius.advancedperks.utilities.MessageConfigReceiver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PerkStateController {

    private static final Logger LOGGER = Bukkit.getLogger();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final SqlConnection sqlConnection;
    private final int globalMaxPerks;

    public PerkStateController() {
        SettingsConfiguration configuration = AdvancedPerks.getInstance().getSettingsConfiguration();
        if (configuration.getSqlType().equals(SqlType.DATABASE)) {
            this.sqlConnection = new SqlConnection(configuration.getSqlUrl(),
                    configuration.getSqlUserName(), configuration.getSqlPassword());
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
            this.sqlConnection = new SqlConnection(String.format("jdbc:sqlite:%s", databaseFile.getPath()));
            LOGGER.log(Level.INFO, "Successfully connected to the local file based database.");
        }
        this.getSqlConnection().customQuery("CREATE TABLE IF NOT EXISTS activated_perks(UUID varchar(36) PRIMARY KEY,PERKS varchar(999))");
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
            ResultSet resultSet = this.getSqlConnection().selectQuery(Collections.singletonList("PERKS"), "activated_perks", "UUID = '" + uuid + "'");
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
                    ignored.printStackTrace();
                }
            });
        });
    }

    public void savePerkData(PerkData perkData) {
        String uuid = perkData.getPlayer().getUniqueId().toString();
        String activatedPerks = perkData.getActivatedPerks().stream().map(Perk::getIdentifier).collect(Collectors.joining(","));
        this.getExecutorService().submit(() -> {
            this.getSqlConnection().insertOrUpdateQuery("activated_perks", Arrays.asList("UUID", "PERKS"),
                    Arrays.asList(uuid, activatedPerks), "UUID = '" + uuid + "'",
                    Collections.singletonList("PERKS = '" + activatedPerks + "'"));
        });
    }

    public void savePerkDataAsync(PerkData perkData) {
        this.savePerkData(perkData);
    }

    public void handleShutdown() {
        AdvancedPerks.getInstance().getPerkDataRepository().getPerkDataCache().values().forEach(this::savePerkData);
        this.getSqlConnection().closeConnection();
    }

    /* the getter and setter of this class */

    public int getGlobalMaxPerks() {
        return globalMaxPerks;
    }

    public SqlConnection getSqlConnection() {
        return sqlConnection;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
