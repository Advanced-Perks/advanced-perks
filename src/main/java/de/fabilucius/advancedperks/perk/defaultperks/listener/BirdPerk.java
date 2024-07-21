package de.fabilucius.advancedperks.perk.defaultperks.listener;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.data.PerkDataRepository;
import de.fabilucius.advancedperks.perk.AbstractDefaultPerk;
import de.fabilucius.advancedperks.perk.annotation.PerkIdentifier;
import de.fabilucius.advancedperks.perk.properties.PerkDescription;
import de.fabilucius.advancedperks.perk.properties.PerkGuiIcon;
import de.fabilucius.advancedperks.perk.types.ListenerPerk;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import java.util.Map;

@PerkIdentifier("bird")
public class BirdPerk extends AbstractDefaultPerk implements ListenerPerk {

    @Inject
    private PerkDataRepository perkDataRepository;

    public BirdPerk(String identifier, String displayName, PerkDescription perkDescription, PerkGuiIcon perkGuiIcon, boolean enabled, Map<String, Object> flags) {
        super(identifier, displayName, perkDescription, perkGuiIcon, enabled, flags);
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        PerkData perkData = this.perkDataRepository.getPerkDataByPlayer(player);
        if (perkData.isPerkEnabled(this)) {
            this.enableFlying(player);
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        PerkData perkData = this.perkDataRepository.getPerkDataByPlayer(player);
        if (perkData.isPerkEnabled(this)) {
            this.enableFlying(player);
        }
    }

    @Override
    public void onPerkEnable(Player player) {
        this.enableFlying(player);
    }

    @Override
    public void onPerkDisable(Player player) {
        if (player.getGameMode().equals(GameMode.SURVIVAL) || player.getGameMode().equals(GameMode.ADVENTURE)) {
            player.setAllowFlight(false);
            player.setFlying(false);
        }
    }

    private void enableFlying(Player player) {
        player.setAllowFlight(true);
        player.setFlying(true);
    }
}
