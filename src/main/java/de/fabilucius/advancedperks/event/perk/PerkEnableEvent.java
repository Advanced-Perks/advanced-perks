package de.fabilucius.advancedperks.event.perk;

import de.fabilucius.advancedperks.perk.Perk;
import org.bukkit.entity.Player;

public class PerkEnableEvent extends PerkToggleEvent {

    public PerkEnableEvent(Player player, Perk perk, boolean forced) {
        super(player, perk, forced, ToggleType.ENABLE);
    }
}
