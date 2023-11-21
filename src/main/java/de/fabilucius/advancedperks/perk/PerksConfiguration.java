package de.fabilucius.advancedperks.perk;

import com.google.common.collect.Maps;
import de.fabilucius.advancedperks.configuration.annotation.FilePathInJar;
import de.fabilucius.advancedperks.configuration.types.MessageConfiguration;
import de.fabilucius.advancedperks.perk.properties.PerkDescription;
import de.fabilucius.advancedperks.perk.properties.PerkGuiIcon;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;
import java.util.stream.Collectors;

@FilePathInJar("perks.yml")
public class PerksConfiguration extends MessageConfiguration {

    public String getDisplayName(String identifier) {
        return this.getComputedString(identifier + ".display_name");
    }

    public PerkDescription getPerkDescription(String identifier) {
        return new PerkDescription(this.getComputedStringList(identifier + ".description"));
    }

    //TODO ICON LOGIC
    public PerkGuiIcon getPerkGuiIcon(String identifier) {
        return new PerkGuiIcon();
    }

    public boolean isEnabled(String identifier) {
        return this.getBoolean(identifier + ".enabled");
    }

    public Map<String, Object> getFlags(String identifier) {
        ConfigurationSection configurationSection = this.getConfigurationSection(identifier + ".flags");
        return configurationSection == null ? Maps.newHashMap() : configurationSection.getKeys(false).stream()
                .filter(line -> this.get(identifier + ".flags." + line) != null)
                .collect(Collectors.toMap(line -> line, line -> this.get(identifier + ".flags." + line)));
    }
}
