package de.fabilucius.advancedperks.settings;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.sympel.configuration.types.PluginConfiguration;

public class MessageConfiguration extends PluginConfiguration {
    public MessageConfiguration() {
        super(AdvancedPerks.getInstance(), "message.yml");
    }

}
