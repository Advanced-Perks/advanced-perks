package de.fabilucius.advancedperks.event.perk;

import de.fabilucius.advancedperks.perk.Perk;
import org.bukkit.entity.Player;

public class PerkDisableEvent extends PerkToggleEvent {
    public PerkDisableEvent(Player player, Perk perk, boolean forced) {
        super(player, perk, forced, ToggleType.DISABLE);
    }
}
