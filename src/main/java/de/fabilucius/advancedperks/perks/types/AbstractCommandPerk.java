package de.fabilucius.advancedperks.perks.types;

import de.fabilucius.advancedperks.perks.AbstractPerk;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class AbstractCommandPerk extends AbstractPerk {

    private final List<String> enableCommands;
    private final List<String> disableCommands;

    public AbstractCommandPerk(String identifier, List<String> enableCommands, List<String> disableCommands) {
        super(identifier);
        this.enableCommands = enableCommands;
        this.disableCommands = disableCommands;
    }

    public AbstractCommandPerk(String identifier, String displayName, String permission, List<String> description, List<String> enableCommands, List<String> disableCommands) {
        super(identifier, displayName, permission, description);
        this.enableCommands = enableCommands;
        this.disableCommands = disableCommands;
    }

    @Override
    public final void prePerkEnable(Player player) {
        this.getEnableCommands().forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
        super.prePerkEnable(player);
    }

    @Override
    public final void prePerkDisable(Player player) {
        this.getDisableCommands().forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
        super.prePerkDisable(player);
    }

    /* the getter and setter of this class */

    public List<String> getEnableCommands() {
        return enableCommands;
    }

    public List<String> getDisableCommands() {
        return disableCommands;
    }
}
