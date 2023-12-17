package de.fabilucius.advancedperks.perk;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import de.fabilucius.advancedperks.core.configuration.annotation.FilePathInJar;
import de.fabilucius.advancedperks.core.configuration.types.AbstractMessageConfiguration;
import de.fabilucius.advancedperks.core.MessagesConfiguration;
import de.fabilucius.advancedperks.perk.properties.PerkDescription;
import de.fabilucius.advancedperks.perk.properties.PerkGuiIcon;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;
import java.util.stream.Collectors;

@FilePathInJar("perks.yml")
public class PerksConfiguration extends AbstractMessageConfiguration {

    private final MessagesConfiguration messagesConfiguration;

    @Inject
    public PerksConfiguration(MessagesConfiguration messagesConfiguration) {
        this.messagesConfiguration = messagesConfiguration;
    }

    public String getDisplayName(String identifier) {
        return this.getComputedString(identifier + ".display_name");
    }

    public PerkDescription getPerkDescription(String identifier) {
        return new PerkDescription(this.getComputedStringList(identifier + ".description"));
    }

    public PerkGuiIcon getPerkGuiIcon(String identifier) {
        return new PerkGuiIcon(this.getString(identifier + ".icon"));
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

    public Map<String, Object> setPrice(String identifier, Double price) {
        Map<String, Object> flags = this.getFlags(identifier);
        flags.put("price", price);
        this.set(identifier + ".flags", flags);
        return flags;
    }

    @Override
    public String getPrefix() {
        return this.messagesConfiguration.getPrefix();
    }
}
