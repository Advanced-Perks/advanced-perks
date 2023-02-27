package de.fabilucius.advancedperks.perks;

import de.fabilucius.advancedperks.commons.configuration.value.types.SingleValue;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface Perk {

    String getIdentifier();

    String getDisplayName();

    String getPermission();

    List<String> getDescription();

    List<String> getDisabledWorlds();

    ItemStack getIcon();

    boolean isEnabled();

    void perkEnable(Player player);

    void prePerkEnable(Player player);

    void perkDisable(Player player);

    void prePerkDisable(Player player);

    SingleValue<Number> getPrice();
}
