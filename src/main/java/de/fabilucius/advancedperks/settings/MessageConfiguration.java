package de.fabilucius.advancedperks.settings;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.configuration.types.MessageConfig;
import de.fabilucius.advancedperks.commons.configuration.value.types.SingleValue;
import org.bukkit.ChatColor;

public class MessageConfiguration extends MessageConfig {
    public MessageConfiguration() {
        super(AdvancedPerks.getInstance(), "message.yml", "<prefix>", '&');
    }

    @Override
    public String getPrefix() {
        SingleValue<String> prefix = new SingleValue<>(this, "Prefix", "The prefix for all messages of the plugin.", String.class, "§8[§6Advanced Perks§8]");
        return ChatColor.translateAlternateColorCodes('&', prefix.get());
    }
}
