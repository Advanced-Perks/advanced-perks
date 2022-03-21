package de.fabilucius.advancedperks.perks;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.sympel.configuration.AbstractConfig;
import de.fabilucius.sympel.configuration.value.types.ListValue;
import de.fabilucius.sympel.configuration.value.types.SingleValue;
import de.fabilucius.sympel.item.builder.types.ItemStackBuilder;
import de.fabilucius.sympel.item.external.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PerksConfiguration extends AbstractConfig {
    public PerksConfiguration() {
        super(AdvancedPerks.getInstance(), "perks.yml");
    }

    public String getDisplayName(Perk perk) {
        SingleValue<String> displayName = new SingleValue<>(this, perk.getIdentifier() + ".Display-Name", "The displayname primarily used for ingame guis of the perk, this value is mandatory.", String.class, " unable to load displayname for " + perk.getIdentifier());
        return ChatColor.translateAlternateColorCodes('&', displayName.get());
    }

    public String getPermission(Perk perk) {
        SingleValue<String> permission = new SingleValue<>(this, perk.getIdentifier() + ".Permission", "The permission to be able to use the perk when not unlocked, an empty string will mean no permission required.", String.class, "");
        return permission.get();
    }

    public List<String> getDescription(Perk perk) {
        ListValue<String> description = new ListValue<>(this, perk.getIdentifier() + ".Description", "The description for the perk, primarily used in ingame guis, every list element is a new line.", String.class, Collections.emptyList());
        return description.get().stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList());
    }

    public List<String> getDisabledWorlds(Perk perk) {
        ListValue<String> disabledWorlds = new ListValue<>(this, perk.getIdentifier() + ".Disabled-Worlds", "A list of names from worlds in which this perk cannot be enabled.", String.class, Collections.emptyList());
        return disabledWorlds.get();
    }

    public boolean isEnabled(Perk perk) {
        SingleValue<Boolean> enabled = new SingleValue<>(this, perk.getIdentifier() + ".Enabled", "Whether or not this perk should be enabled and loaded when starting the plugin.", Boolean.class, true);
        return enabled.get();
    }

    public ItemStack getIcon(Perk perk) {
        String icon = new SingleValue<>(this, perk.getIdentifier() + ".Icon", "The material name of an item that should represent this perk inside guis.", String.class, "").get();
        if (icon.isEmpty()) {
            return null;
        }
        ItemStack parsedItemStack = XMaterial.valueOf(icon).parseItem();
        if (parsedItemStack != null) {
            return ItemStackBuilder.fromItemStack(parsedItemStack)
                    .setDisplayName(perk.getDisplayName())
                    .setDescription(perk.getDescription())
                    .build();
        }
        return null;
    }

}
