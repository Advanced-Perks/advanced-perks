package de.fabilucius.advancedperks.data;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.core.database.Database;
import de.fabilucius.advancedperks.core.logging.APLogger;
import de.fabilucius.advancedperks.perk.Perk;
import de.fabilucius.advancedperks.registry.PerkRegistry;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class PerkDataRepository implements Listener {

    private final AdvancedPerks advancedPerks;

    @Inject
    private APLogger logger;

    @Inject
    private Database database;

    @Inject
    private PerkRegistry perkRegistry;

    private final Cache<UUID, PerkData> perkDataCache = CacheBuilder.newBuilder().build();

    public void setupDatabase() {
        this.database.connectToDatabase();
        this.database.runSqlScript("sql/2023.sql");
    }

    @Inject
    public PerkDataRepository(AdvancedPerks advancedPerks) {
        this.advancedPerks = advancedPerks;
        Bukkit.getPluginManager().registerEvents(this, this.advancedPerks);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PerkData perkData = new PerkData(event.getPlayer().getUniqueId());
        this.loadPerkDataAsync(perkData);
        this.perkDataCache.put(perkData.getUuid(), perkData);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PerkData perkData = this.perkDataCache.getIfPresent(event.getPlayer().getUniqueId());
        if (perkData != null) {
            this.savePerkDataAsync(perkData);
            this.perkDataCache.invalidate(perkData.getUuid());
        }
    }

    //TODO looking into splitting it up into a separate task class again
    public void loadPerkDataAsync(PerkData perkData) {
        Bukkit.getScheduler().runTaskAsynchronously(this.advancedPerks, () -> {
            String query = "SELECT * FROM ap_data WHERE unique_id = ?";
            try (PreparedStatement loadStatement = this.database.createPreparedStatement(query)) {
                loadStatement.setString(1, perkData.getUuid().toString());
                ResultSet resultSet = loadStatement.executeQuery();
                /* ResultSet is only expected to have one entry */
                if (resultSet.next()) {
                    /* Enabled perks */
                    List<Perk> enabledPerks = Arrays.stream(resultSet.getString("enabled_perks").split(","))
                            .map(perkIdentifier -> this.perkRegistry.getPerkByIdentifier(perkIdentifier))
                            .filter(Objects::nonNull)
                            .toList();
                    //TODO force enable perks sync
                    perkData.getBoughtPerks().addAll(Arrays.stream(resultSet.getString("bought_perks").split(",")).toList());
                    perkData.setDataHash(resultSet.getBytes("data_hash"));
                } else {
                    perkData.setLoaded();
                }
                perkData.getBoughtPerks().add("TestLOL");
            } catch (Exception exception) {
                this.logger.log(Level.WARNING, "An error occurred while loading the PerkData for uniqueId %s.".formatted(perkData.getUuid().toString()), exception);
            }
        });
    }

    public void savePerkDataAsync(PerkData perkData) {
        Bukkit.getScheduler().runTaskAsynchronously(this.advancedPerks, () -> {
            /* Small check to find out if perk data needs to be saved to the database */
            if (!perkData.isLoaded() || Arrays.equals(perkData.getDataHash(), perkData.calculateDataHash())) {
                return;
            }
            String saveQuery = "INSERT INTO ap_data(unique_id, enabled_perks, bought_perks, data_hash) VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE enabled_perks = ?, bought_perks = ?, data_hash = ?";
            try (PreparedStatement saveStatement = this.database.createPreparedStatement(saveQuery)) {
                String enabledPerks = perkData.getEnabledPerks().stream().map(Perk::getIdentifier).collect(Collectors.joining(","));
                String boughtPerks = String.join(",", perkData.getBoughtPerks());
                saveStatement.setString(1, perkData.getUuid().toString());
                saveStatement.setString(2, enabledPerks);
                saveStatement.setString(3, boughtPerks);
                saveStatement.setBytes(4, perkData.calculateDataHash());
                saveStatement.setString(5, enabledPerks);
                saveStatement.setString(6, boughtPerks);
                saveStatement.setBytes(7, perkData.calculateDataHash());
                saveStatement.execute();
            } catch (SQLException exception) {
                this.logger.log(Level.WARNING, "An error occurred while saving the PerkData for uniqueId %s.".formatted(perkData.getUuid().toString()), exception);
            }
        });
    }
}
