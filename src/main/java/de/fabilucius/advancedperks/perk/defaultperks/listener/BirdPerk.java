package de.fabilucius.advancedperks.perk.defaultperks.listener;

import de.fabilucius.advancedperks.perk.AbstractDefaultPerk;
import de.fabilucius.advancedperks.perk.annotation.PerkIdentifier;
import de.fabilucius.advancedperks.perk.properties.PerkDescription;
import de.fabilucius.advancedperks.perk.properties.PerkGuiIcon;
import de.fabilucius.advancedperks.perk.types.ListenerPerk;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Map;

@PerkIdentifier("bird")
public class BirdPerk extends AbstractDefaultPerk implements ListenerPerk {

    public BirdPerk(String identifier, String displayName, PerkDescription perkDescription, PerkGuiIcon perkGuiIcon, boolean enabled, Map<String, Object> flags) {
        super(identifier, displayName, perkDescription, perkGuiIcon, enabled, flags);
    }

    @Override
    public void onPerkEnable(Player player) {
        player.setAllowFlight(true);
        player.setFlying(true);
    }

    @Override
    public void onPerkDisable(Player player) {
        if (player.getGameMode().equals(GameMode.SURVIVAL) || player.getGameMode().equals(GameMode.ADVENTURE)) {
            player.setAllowFlight(false);
            player.setFlying(false);
        }
    }
}
