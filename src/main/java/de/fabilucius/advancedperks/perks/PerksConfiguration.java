package de.fabilucius.advancedperks.perks;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.utilities.ItemStackBuilder;
import de.fabilucius.advancedperks.utilities.XMaterial;
import de.fabilucius.sympel.configuration.types.PluginConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PerksConfiguration extends PluginConfiguration {
    public PerksConfiguration() {
        super(AdvancedPerks.getInstance(), "perks.yml");
    }

    public String getDisplayName(Perk perk) {
        String displayName = this.returnFrom(perk.getIdentifier() + ".Display-Name")
                .getAsWithDefault("unable to load displayname of " + perk.getIdentifier(), String.class);
        return ChatColor.translateAlternateColorCodes('&', displayName);
    }

    public String getPermission(Perk perk) {
        return this.returnFrom(perk.getIdentifier() + ".Permission")
                .getAsWithDefault("", String.class);
    }

    public List<String> getDescription(Perk perk) {
        List<String> description = this.returnFrom(perk.getIdentifier() + ".Description")
                .getAsWithDefault(Collections.singletonList("unable to load description of " + perk.getIdentifier()), List.class);
        return description.stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList());
    }

    public List<String> getDisabledWorlds(Perk perk) {
        return this.returnFrom(perk.getIdentifier() + ".Disabled-Worlds")
                .getAsWithDefault(Collections.emptyList(), List.class);
    }

    public boolean isEnabled(Perk perk) {
        return this.returnFrom(perk.getIdentifier() + ".Enabled")
                .getAsWithDefault(true, Boolean.class);
    }

    public ItemStack getIcon(Perk perk) {
        String icon = this.returnFrom(perk.getIdentifier() + ".Icon").getAs(String.class);
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
