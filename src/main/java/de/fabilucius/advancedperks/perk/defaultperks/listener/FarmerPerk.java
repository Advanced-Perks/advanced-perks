package de.fabilucius.advancedperks.perk.defaultperks.listener;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.data.PerkDataRepository;
import de.fabilucius.advancedperks.perk.AbstractDefaultPerk;
import de.fabilucius.advancedperks.perk.annotation.PerkIdentifier;
import de.fabilucius.advancedperks.perk.properties.PerkDescription;
import de.fabilucius.advancedperks.perk.properties.PerkGuiIcon;
import de.fabilucius.advancedperks.perk.types.ListenerPerk;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@PerkIdentifier("farmer")
public class FarmerPerk extends AbstractDefaultPerk implements ListenerPerk {

    @Inject
    private PerkDataRepository perkDataRepository;

    @Inject
    private AdvancedPerks advancedPerks;

    private final Set<Location> pendingReplants = ConcurrentHashMap.newKeySet();

    public FarmerPerk(String identifier, String displayName, PerkDescription perkDescription, PerkGuiIcon perkGuiIcon, boolean enabled, Map<String, Object> flags) {
        super(identifier, displayName, perkDescription, perkGuiIcon, enabled, flags);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!(event.getBlock().getBlockData() instanceof Ageable ageable) ||
                ageable.getAge() != ageable.getMaximumAge()) {
            return;
        }
        PerkData perkData = this.perkDataRepository.getPerkDataByPlayer(event.getPlayer());
        if (perkData.getEnabledPerks().contains(this)) {
            Location location = event.getBlock().getLocation();
            Material crop = event.getBlock().getType();

            if (!pendingReplants.add(location)) {
                return;
            }

            Bukkit.getScheduler().runTaskLater(this.advancedPerks, () -> {
                try {
                    Block block = location.getBlock();
                    if (block.getType() == Material.AIR) {
                        block.setType(crop);
                    }
                } finally {
                    pendingReplants.remove(location);
                }
            }, 1L);
        }
    }

}
