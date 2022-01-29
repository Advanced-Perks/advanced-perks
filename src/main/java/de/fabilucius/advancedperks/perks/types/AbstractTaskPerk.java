package de.fabilucius.advancedperks.perks.types;

import de.fabilucius.advancedperks.perks.AbstractPerk;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public abstract class AbstractTaskPerk extends AbstractPerk {

    private final BukkitTask bukkitTask;

    public AbstractTaskPerk(String identifier, BukkitTask bukkitTask) {
        super(identifier);
        this.bukkitTask = bukkitTask;
    }

    public AbstractTaskPerk(String identifier, String displayName, String permission, List<String> description, BukkitTask bukkitTask) {
        super(identifier, displayName, permission, description);
        this.bukkitTask = bukkitTask;
    }

    /* the getter and setter of this class */

    public BukkitTask getBukkitTask() {
        return bukkitTask;
    }
}
