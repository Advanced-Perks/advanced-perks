package de.fabilucius.advancedperks.data;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.MapCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class PerkDataRepository extends MapCache<Player, PerkData> implements Listener {

    private PerkDataRepository() {
        Bukkit.getPluginManager().registerEvents(this, AdvancedPerks.getInstance());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.addPerkData(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.removePerkData(event.getPlayer());
    }

    public void consumePerkData(Player player, Consumer<PerkData> perkData) {
        PerkData data = this.getPerkData(player);
        if (data != null) {
            perkData.accept(data);
        }
    }

    public Optional<PerkData> getPerkDataByUuid(UUID uuid) {
        return this.getPerkDataCache().entrySet().stream().filter(playerPerkDataEntry ->
                playerPerkDataEntry.getKey().getUniqueId().equals(uuid)).findFirst().map(Map.Entry::getValue);
    }

    public PerkData getPerkData(Player player) {
        if (!this.getPerkDataCache().containsKey(player)) {
            PerkData perkData = new PerkData(player);
            this.getPerkDataCache().put(player, perkData);
            return perkData;
        }
        return this.getPerkDataCache().get(player);
    }

    private void addPerkData(Player player) {
        if (!this.getPerkDataCache().containsKey(player)) {
            this.getPerkDataCache().put(player, new PerkData(player));
        }
    }

    private void removePerkData(Player player) {
        PerkData perkData = this.getPerkData(player);
        AdvancedPerks.getInstance().getPerkStateController().savePerkData(perkData);
        AdvancedPerks.getInstance().getPerkStateController().disableAllPerks(player);
        this.getPerkDataCache().remove(player);
    }

    public Map<Player, PerkData> getPerkDataCache() {
        return super.getCache();
    }

    /* Singleton stuff */

    private static PerkDataRepository instance;

    public static PerkDataRepository getSingleton() {
        if (instance == null) {
            instance = new PerkDataRepository();
        }
        return instance;
    }

}
