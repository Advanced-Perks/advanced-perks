package de.fabilucius.advancedperks.commons.configuration.types;

import de.fabilucius.advancedperks.commons.configuration.AbstractConfig;
import de.fabilucius.advancedperks.commons.configuration.utilities.ReplaceLogic;
import de.fabilucius.advancedperks.commons.configuration.value.types.ListValue;
import de.fabilucius.advancedperks.commons.configuration.value.types.SingleValue;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MessageConfig extends AbstractConfig {

    private final char colorCodeChar;
    private final String prefixReplaceKey;

    public MessageConfig(@NotNull Plugin plugin, @NotNull String fileName, @NotNull String prefixReplaceKey, @NotNull char colorCodeChar) {
        super(plugin, fileName);
        this.prefixReplaceKey = prefixReplaceKey;
        this.colorCodeChar = colorCodeChar;
    }

    public abstract String getPrefix();

    public final String getMessage(String configurationKey, ReplaceLogic... replaceLogics) {
        SingleValue<String> messageValue = new SingleValue<>(this, configurationKey, "Message of " + configurationKey, String.class, "message " + configurationKey + " from " + this.getClass().getName() + " couldn't be loaded");
        String message = messageValue.get();
        for (ReplaceLogic replaceLogic : replaceLogics) {
            message = message.replaceAll(replaceLogic.getReplaceKey(), replaceLogic.getReplaceContent());
        }
        message = message.replaceAll(this.prefixReplaceKey, this.getPrefix());
        return ChatColor.translateAlternateColorCodes(this.colorCodeChar, message);
    }

    public final List<String> getMessageList(String configurationKey, ReplaceLogic... replaceLogics) {
        ListValue<String> messageValues = new ListValue<>(this, configurationKey, "Messages of " + configurationKey, String.class, Collections.singletonList("messages " + configurationKey + " from " + this.getClass().getName() + " couldn't be loaded"));
        List<String> messages = messageValues.get();
        return messages.stream().map(message -> {
            for (ReplaceLogic replaceLogic : replaceLogics) {
                message = message.replaceAll(replaceLogic.getReplaceKey(), replaceLogic.getReplaceContent());
            }
            message = message.replaceAll(this.prefixReplaceKey, this.getPrefix());
            return ChatColor.translateAlternateColorCodes(this.colorCodeChar, message);
        }).collect(Collectors.toList());
    }

    /* The getter and setter of this class */

    public String getPrefixReplaceKey() {
        return prefixReplaceKey;
    }

    public char getColorCodeChar() {
        return colorCodeChar;
    }
}
