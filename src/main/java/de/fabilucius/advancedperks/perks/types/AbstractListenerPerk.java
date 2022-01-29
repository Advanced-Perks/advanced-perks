package de.fabilucius.advancedperks.perks.types;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.perks.AbstractPerk;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.List;

public abstract class AbstractListenerPerk extends AbstractPerk implements Listener {

    public AbstractListenerPerk(String identifier) {
        super(identifier);
        Bukkit.getPluginManager().registerEvents(this, AdvancedPerks.getInstance());
    }

    public AbstractListenerPerk(String identifier, String displayName, String permission, List<String> description) {
        super(identifier, displayName, permission, description);
        Bukkit.getPluginManager().registerEvents(this, AdvancedPerks.getInstance());
    }
}
