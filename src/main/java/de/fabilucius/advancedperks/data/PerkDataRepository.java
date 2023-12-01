package de.fabilucius.advancedperks.data;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.core.database.Database;
import de.fabilucius.advancedperks.core.logging.APLogger;
import de.fabilucius.advancedperks.data.state.PerkStateController;
import de.fabilucius.advancedperks.perk.Perk;
import de.fabilucius.advancedperks.registry.PerkRegistryImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class PerkDataRepository implements Listener {

    private final AdvancedPerks advancedPerks;

    @Inject
    private APLogger logger;

    @Inject
    private Database database;

    @Inject
    private PerkRegistryImpl perkRegistryImpl;

    @Inject
    private PerkStateController perkStateController;

    private final Cache<UUID, PerkData> perkDataCache = CacheBuilder.newBuilder().build();

    public void setupDatabase() {
        this.database.connectToDatabase();
        this.database.runSqlScript("sql/2023.sql");
    }

    public void loadOnlinePlayer() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            this.getPerkDataByUuid(player.getUniqueId());
        });
    }

    @NotNull
    public CompletableFuture<PerkData> getPerkDataByUuid(UUID uuid) {
        PerkData perkData = this.perkDataCache.getIfPresent(uuid);
        if (perkData != null) {
            return CompletableFuture.supplyAsync(() -> perkData);
        } else {
            return this.loadPerkDataAsync(uuid);
        }
    }

    @NotNull
    public PerkData getPerkDataByPlayer(Player player) {
        PerkData perkData = this.perkDataCache.getIfPresent(player.getUniqueId());
        if (perkData == null) {
            UnloadedPerkData createdPerkData = new UnloadedPerkData(player.getUniqueId());
            this.perkDataCache.put(player.getUniqueId(), createdPerkData);
            this.loadPerkDataAsync(player.getUniqueId()).thenAcceptAsync(perkData1 ->
                    this.perkDataCache.put(perkData1.getUuid(), perkData1));
            return createdPerkData;
        }
        return perkData;
    }

    @Inject
    public PerkDataRepository(AdvancedPerks advancedPerks) {
        this.advancedPerks = advancedPerks;
        Bukkit.getPluginManager().registerEvents(this, this.advancedPerks);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UnloadedPerkData unloadedPerkData = new UnloadedPerkData(event.getPlayer().getUniqueId());
        this.perkDataCache.put(unloadedPerkData.getUuid(), unloadedPerkData);
        this.loadPerkDataAsync(unloadedPerkData.getUuid()).thenAcceptAsync(perkData ->
                this.perkDataCache.put(perkData.getUuid(), perkData));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PerkData perkData = this.perkDataCache.getIfPresent(player.getUniqueId());
        if (perkData != null) {
            this.savePerkDataAsync(perkData);
            //TODO currently unneeded is bound to cause some issues/bugs later on needs refactoring
            perkData.getEnabledPerks().forEach(perk -> perk.onPrePerkDisable(player));
            this.perkDataCache.invalidate(perkData.getUuid());
        }
    }

    //TODO currently unneeded looking into splitting it up into a separate task class again
    public CompletableFuture<PerkData> loadPerkDataAsync(UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            PerkData perkData = new PerkData(uniqueId);
            String query = "SELECT * FROM ap_data WHERE unique_id = ?";
            try (PreparedStatement loadStatement = this.database.createPreparedStatement(query)) {
                loadStatement.setString(1, uniqueId.toString());
                ResultSet resultSet = loadStatement.executeQuery();
                /* ResultSet is only expected to have one entry */
                if (resultSet.next()) {
                    /* Enabled perks */
                    List<Perk> enabledPerks = Arrays.stream(resultSet.getString("enabled_perks").split(","))
                            .map(perkIdentifier -> this.perkRegistryImpl.getPerkByIdentifier(perkIdentifier))
                            .filter(Objects::nonNull)
                            .toList();
                    Bukkit.getScheduler().runTask(this.advancedPerks, () -> {
                        Player player = Bukkit.getPlayer(uniqueId);
                        if (player != null) {
                            enabledPerks.forEach(perk ->
                                    this.perkStateController.enablePerk(player, perk));
                        }
                    });
                    perkData.getBoughtPerks().addAll(Arrays.stream(resultSet.getString("bought_perks").split(",")).toList());
                    perkData.setDataHash(resultSet.getBytes("data_hash"));
                } else {
                    perkData.setLoaded();
                }
            } catch (Exception exception) {
                this.logger.log(Level.WARNING, "An error occurred while loading the PerkData for uniqueId %s.".formatted(perkData.getUuid().toString()), exception);
            }
            return perkData;
        });
    }

    public void savePerkDataAsync(PerkData perkData) {
        Bukkit.getScheduler().runTaskAsynchronously(this.advancedPerks, () -> {
            this.savePerkDataSync(perkData);
        });
    }

    private void savePerkDataSync(PerkData perkData) {
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

    }

    public void handleShutdown() {
        this.perkDataCache.asMap().values().forEach(this::savePerkDataSync);
    }
}
