package de.fabilucius.advancedperks.compatabilities;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.AdvancedPerks;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public abstract class AbstractPerkCompatability implements Listener {

    @Inject
    protected AbstractPerkCompatability(AdvancedPerks advancedPerks) {
        Bukkit.getPluginManager().registerEvents(this, advancedPerks);
    }

}
