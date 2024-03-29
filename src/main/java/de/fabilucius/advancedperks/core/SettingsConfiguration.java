package de.fabilucius.advancedperks.core;

import de.fabilucius.advancedperks.core.configuration.Configuration;
import de.fabilucius.advancedperks.core.configuration.annotation.FilePathInJar;

@FilePathInJar("settings.yml")
public class SettingsConfiguration extends Configuration {

    public int getGlobalMaxActivePerks() {
        return this.getInt("global.max_active_perks", -1);
    }

    public boolean isGuiClickSoundsEnabled() {
        return this.getBoolean("gui.click_sounds", true);
    }

    public boolean isMetricsCollectionEnabled() {
        return this.getBoolean("collecting_metrics", true);
    }
}
