package de.fabilucius.advancedperks.perks;

import de.fabilucius.advancedperks.commons.configuration.Configuration;
import de.fabilucius.advancedperks.utilities.ItemStackBuilder;
import de.fabilucius.advancedperks.utilities.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PerksConfiguration extends Configuration {
    public PerksConfiguration() {
        super("perks");
    }

    public String getDisplayName(Perk perk) {
        String displayName = this.getValueWithDefault(perk.getIdentifier() + ".Display-Name",
                "unable to load displayname of " + perk.getIdentifier(), String.class);
        return ChatColor.translateAlternateColorCodes('&', displayName);
    }

    public String getPermission(Perk perk) {
        return this.getValueWithDefault(perk.getIdentifier() + ".Permission", "", String.class);
    }

    public List<String> getDescription(Perk perk) {
        List<String> description = this.getValueWithDefault(perk.getIdentifier() + ".Description",
                Collections.singletonList("unable to load description of " + perk.getIdentifier()), List.class);
        return description.stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList());
    }

    public List<String> getDisabledWorlds(Perk perk) {
        return this.getValueWithDefault(perk.getIdentifier() + ".Disabled-Worlds",
                Collections.emptyList(), List.class);
    }

    public boolean isEnabled(Perk perk) {
        return this.getValueWithDefault(perk.getIdentifier() + ".Enabled", true, Boolean.class);
    }

    public ItemStack getIcon(Perk perk) {
        String icon = this.getValue(perk.getIdentifier() + ".Icon", String.class);
        if (icon == null || icon.isEmpty()) {
            return null;
        }
        ItemStack parsedItemStack = XMaterial.valueOf(icon).parseItem();
        if (parsedItemStack != null) {
            return new ItemStackBuilder(parsedItemStack)
                    .setDisplayName(perk.getDisplayName())
                    .setDescription(perk.getDescription())
                    .build();
        }
        return null;
    }

}
