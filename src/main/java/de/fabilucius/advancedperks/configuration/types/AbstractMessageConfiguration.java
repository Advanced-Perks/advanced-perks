package de.fabilucius.advancedperks.configuration.types;

import de.fabilucius.advancedperks.configuration.Configuration;
import de.fabilucius.advancedperks.configuration.replace.ReplaceOptions;
import org.bukkit.ChatColor;

import java.util.List;

public abstract class AbstractMessageConfiguration extends Configuration {

    public String getComputedString(String key, ReplaceOptions... replaceOptions) {
        String message = this.getString(key, "message for key %s wasn't found".formatted(key));
        for (ReplaceOptions replaceOption : replaceOptions) {
            message = message.replaceAll(replaceOption.replaceKey(), replaceOption.replaceData());
        }
        message = message.replace("<prefix>", this.getPrefix());
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public List<String> getComputedStringList(String key, ReplaceOptions... replaceOptions) {
        List<String> messages = this.getStringList(key);
        if (messages.isEmpty()) {
            messages.add("message list for key %s wasn't found".formatted(key));
            return messages;
        }
        return messages.stream().map(message -> {
            for (ReplaceOptions replaceOption : replaceOptions) {
                message = message.replace(replaceOption.replaceKey(), replaceOption.replaceData());
            }
            message = message.replace("<prefix>", this.getPrefix());
            return ChatColor.translateAlternateColorCodes('&', message);
        }).toList();
    }

    public abstract String getPrefix();
}

