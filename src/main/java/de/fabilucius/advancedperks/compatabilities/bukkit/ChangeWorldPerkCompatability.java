package de.fabilucius.advancedperks.compatabilities.bukkit;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.compatabilities.AbstractPerkCompatability;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.data.PerkDataRepository;
import de.fabilucius.advancedperks.data.state.PerkStateController;
import de.fabilucius.advancedperks.perk.defaultperks.listener.BirdPerk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;


@Singleton
public class ChangeWorldPerkCompatability extends AbstractPerkCompatability {

    @Inject
    private PerkDataRepository perkDataRepository;

    @Inject
    private PerkStateController perkStateController;

    @Inject
    public ChangeWorldPerkCompatability(AdvancedPerks advancedPerks) {
        super(advancedPerks);
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        PerkData perkData = this.perkDataRepository.getPerkDataByPlayer(player);

        perkData.getEnabledPerks().stream().filter(BirdPerk.class::isInstance).findAny()
                .ifPresent(perk -> perk.onPrePerkEnable(player));

        perkData.getEnabledPerks().stream()
                .filter(perk -> perk.getDisallowedWorlds().isPresent() && perk.getDisallowedWorlds().get().contains(player.getWorld().getName()))
                .forEach(perk -> this.perkStateController.disablePerk(player, perk));
    }

}
