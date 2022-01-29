package de.fabilucius.advancedperks.commons;

import de.fabilucius.advancedperks.AdvancedPerks;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class BukkitListener implements Listener {

    public BukkitListener() {
        Bukkit.getPluginManager().registerEvents(this, AdvancedPerks.getInstance());
    }

}
