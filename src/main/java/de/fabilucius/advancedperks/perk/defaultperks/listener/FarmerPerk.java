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
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Map;

@PerkIdentifier("farmer")
public class FarmerPerk extends AbstractDefaultPerk implements ListenerPerk {

    @Inject
    private PerkDataRepository perkDataRepository;

    @Inject
    private AdvancedPerks advancedPerks;

    public FarmerPerk(String identifier, String displayName, PerkDescription perkDescription, PerkGuiIcon perkGuiIcon, boolean enabled, Map<String, Object> flags) {
        super(identifier, displayName, perkDescription, perkGuiIcon, enabled, flags);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!(event.getBlock().getBlockData() instanceof Ageable ageable) ||
                ageable.getAge() != ageable.getMaximumAge()) {
            return;
        }
        PerkData perkData = this.perkDataRepository.getPerkDataByPlayer(event.getPlayer());
        if (perkData.getEnabledPerks().contains(this)) {
            Material crop = event.getBlock().getType();
            Bukkit.getScheduler().runTaskLater(this.advancedPerks, () ->
                    event.getBlock().setType(crop), 1L);
        }
    }

}
