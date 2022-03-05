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
    private PerkDataRepository perkDataRepository;
    private SettingsConfiguration settingsConfiguration;
    private PerksConfiguration perksConfiguration;
    private MessageConfiguration messageConfiguration;
    private PerkStateController perkStateController;
    private PerkListCache perkRegistry;
    private GuiManager guiManager;
    private CompatabilityController compatabilityController;

    @Override
    public void onEnable() {
        this.settingsConfiguration = new SettingsConfiguration();
        this.perksConfiguration = new PerksConfiguration();
        this.perkDataRepository = new PerkDataRepository();
        this.messageConfiguration = new MessageConfiguration();
        this.perkRegistry = new PerkListCache();
        this.guiManager = new GuiManager();
        this.perkStateController = new PerkStateController();
        this.compatabilityController = new CompatabilityController();
        new PerksCommand();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new AdvancedPerksExpansion().register();
        }
        if (this.getSettingsConfiguration().METRICS_ENABLED.get()) {
            new Metrics(this, 12771);
        }
    }

    @Override
    public void onDisable() {
        this.getPerkStateController().handleShutdown();
        this.getGuiManager().handleShutdown();
    }

    /* the getter and setter of this class */

    public static AdvancedPerks getInstance() {
        return getPlugin(AdvancedPerks.class);
    }

    public SettingsConfiguration getSettingsConfiguration() {
        return settingsConfiguration;
    }

    public PerksConfiguration getPerksConfiguration() {
        return perksConfiguration;
    }

    public PerkDataRepository getPerkDataRepository() {
        return perkDataRepository;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }

    public MessageConfiguration getMessageConfiguration() {
        return messageConfiguration;
    }

    public PerkListCache getPerkRegistry() {
        return perkRegistry;
    }

    public PerkStateController getPerkStateController() {
        return perkStateController;
    }

    public CompatabilityController getCompatabilityController() {
        return compatabilityController;
    }
}
