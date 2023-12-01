package de.fabilucius.advancedperks.event.perk;

import de.fabilucius.advancedperks.event.AdvancedPerksEvent;
import de.fabilucius.advancedperks.perk.Perk;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PerkToggleEvent extends AdvancedPerksEvent implements Cancellable {

    private final Player player;
    private final Perk perk;
    private final boolean forced;
    private final ToggleType toggleType;
    private boolean cancelled;

    public PerkToggleEvent(Player player, Perk perk, boolean forced, ToggleType toggleType) {
        this.player = player;
        this.perk = perk;
        this.forced = forced;
        this.toggleType = toggleType;
    }

    public Player getPlayer() {
        return player;
    }

    public Perk getPerk() {
        return perk;
    }

    public boolean isForced() {
        return forced;
    }

    public ToggleType getToggleType() {
        return toggleType;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
