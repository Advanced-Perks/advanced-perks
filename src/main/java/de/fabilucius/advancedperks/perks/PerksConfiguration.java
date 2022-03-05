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
        SingleValue<String> displayName = new SingleValue<>(this, perk.getIdentifier() + ".Display-Name", "couldn't load displayname", String.class, "Set description");
        return ChatColor.translateAlternateColorCodes('&', displayName.get());
    }

    public String getPermission(Perk perk) {
        SingleValue<String> permission = new SingleValue<>(this, perk.getIdentifier() + ".Permission", "", String.class, "Set description");
        return permission.get();
    }

    public List<String> getDescription(Perk perk) {
        ListValue<String> description = new ListValue<>(this, perk.getIdentifier() + ".Description", "Set descriptioin", String.class, Collections.emptyList());
        return description.get().stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList());
    }

    public List<String> getDisabledWorlds(Perk perk) {
        ListValue<String> disabledWorlds = new ListValue<>(this, perk.getIdentifier() + ".Disabled-Worlds", "Set descriptioin", String.class, Collections.emptyList());
        return disabledWorlds.get();
    }

    public boolean isEnabled(Perk perk) {
        SingleValue<Boolean> enabled = new SingleValue<>(this, perk.getIdentifier() + ".Enabled", "Set descriotun", Boolean.class, true);
        return enabled.get();
    }

    public ItemStack getIcon(Perk perk) {
        String icon = new SingleValue<>(this, perk.getIdentifier() + ".Icon", "Desciripotion", String.class, null).get();
        if (icon == null || icon.isEmpty()) {
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
