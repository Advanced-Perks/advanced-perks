package de.fabilucius.advancedperks.guisystem.configuration;

import org.bukkit.ChatColor;

public enum PerkGuiSaveResult {

    SUCCESS(""),
    MISSING_ELEMENTS(ChatColor.GRAY + "Cannot save not all elements are inside the gui."),
    ERROR("An error occurred check the server log.");

    private final String message;

    PerkGuiSaveResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
