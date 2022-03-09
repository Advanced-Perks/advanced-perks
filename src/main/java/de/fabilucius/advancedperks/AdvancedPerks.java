package de.fabilucius.advancedperks;

import de.fabilucius.advancedperks.api.AdvancedPerksExpansion;
import de.fabilucius.advancedperks.commands.PerksCommand;
import de.fabilucius.advancedperks.commons.Metrics;
import de.fabilucius.advancedperks.commons.guisystem.management.GuiManager;
import de.fabilucius.advancedperks.compatability.CompatabilityController;
import de.fabilucius.advancedperks.data.PerkDataRepository;
import de.fabilucius.advancedperks.perks.PerkListCache;
import de.fabilucius.advancedperks.perks.PerkStateController;
import de.fabilucius.advancedperks.perks.PerksConfiguration;
import de.fabilucius.advancedperks.settings.MessageConfiguration;
import de.fabilucius.advancedperks.settings.SettingsConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class AdvancedPerks extends JavaPlugin {

    public static final Logger LOGGER = Bukkit.getLogger();
    private static PerkDataRepository perkDataRepository;
    private static SettingsConfiguration settingsConfiguration;
    private static PerksConfiguration perksConfiguration;
    private static MessageConfiguration messageConfiguration;
    private static PerkStateController perkStateController;
    private static PerkListCache perkRegistry;
    private static GuiManager guiManager;
    private static CompatabilityController compatabilityController;

    @Override
    public void onEnable() {
        settingsConfiguration = new SettingsConfiguration();
        perksConfiguration = new PerksConfiguration();
        perkDataRepository = new PerkDataRepository();
        messageConfiguration = new MessageConfiguration();
        perkRegistry = new PerkListCache();
        guiManager = new GuiManager();
        perkStateController = new PerkStateController();
        compatabilityController = new CompatabilityController();
        new PerksCommand();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new AdvancedPerksExpansion().register();
        }
        if (getSettingsConfiguration().METRICS_ENABLED.get()) {
            new Metrics(this, 12771);
        }
    }

    @Override
    public void onDisable() {
        getPerkStateController().handleShutdown();
        getGuiManager().handleShutdown();
    }

    /* the getter and setter of this class */

    public static AdvancedPerks getInstance() {
        return getPlugin(AdvancedPerks.class);
    }

    public static SettingsConfiguration getSettingsConfiguration() {
        return settingsConfiguration;
    }

    public static PerksConfiguration getPerksConfiguration() {
        return perksConfiguration;
    }

    public static PerkDataRepository getPerkDataRepository() {
        return perkDataRepository;
    }

    public static GuiManager getGuiManager() {
        return guiManager;
    }

    public static MessageConfiguration getMessageConfiguration() {
        return messageConfiguration;
    }

    public static PerkListCache getPerkRegistry() {
        return perkRegistry;
    }

    public static PerkStateController getPerkStateController() {
        return perkStateController;
    }

    public static CompatabilityController getCompatabilityController() {
        return compatabilityController;
    }
}
