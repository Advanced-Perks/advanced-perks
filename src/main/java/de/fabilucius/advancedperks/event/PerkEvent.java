package de.fabilucius.advancedperks.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PerkEvent extends Event {

    public static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
