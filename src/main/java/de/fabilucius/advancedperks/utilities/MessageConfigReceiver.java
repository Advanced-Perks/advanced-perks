package de.fabilucius.advancedperks.utilities;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.ReplaceLogic;
import de.fabilucius.advancedperks.settings.MessageConfiguration;
import org.bukkit.ChatColor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MessageConfigReceiver {

    private static final MessageConfiguration MESSAGE_CONFIGURATION = AdvancedPerks.getInstance().getMessageConfiguration();

    public static String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&',
                MESSAGE_CONFIGURATION.getValueWithDefault("Prefix", "§8[§6APerks§8]", String.class));
    }

    public static String getMessage(String configKey) {
        return ChatColor.translateAlternateColorCodes('&', MESSAGE_CONFIGURATION.getValueWithDefault(configKey,
                "unable to load message", String.class).replaceAll("<prefix>", getPrefix()));
    }

    public static String getMessageWithReplace(String configKey, ReplaceLogic... replaceLogics) {
        String message = getMessage(configKey);
        for (ReplaceLogic replaceLogic : replaceLogics) {
            message = message.replaceAll(replaceLogic.getReplaceKey(), replaceLogic.getReplaceContent());
        }
        return message;
    }

    public static List<String> getMessageList(String configKey) {
        List<String> rawMessages = MESSAGE_CONFIGURATION.getValueWithDefault(configKey,
                Collections.singletonList("unable to load messages"), List.class);
        return rawMessages.stream().map(line -> ChatColor.translateAlternateColorCodes('&',
                line).replaceAll("<prefix>", getPrefix())).collect(Collectors.toList());
    }

}
