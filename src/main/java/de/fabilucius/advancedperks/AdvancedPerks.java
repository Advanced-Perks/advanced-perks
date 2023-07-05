package de.fabilucius.advancedperks;

import de.fabilucius.advancedperks.api.placeholderapi.AdvancedPerksExpansion;
import de.fabilucius.advancedperks.commands.PerksCommand;
import de.fabilucius.advancedperks.commons.Metrics;
import de.fabilucius.advancedperks.commons.guisystem.management.GuiManager;
import de.fabilucius.advancedperks.compatability.CompatabilityController;
import de.fabilucius.advancedperks.data.PerkDataRepository;
import de.fabilucius.advancedperks.economy.EconomyController;
import de.fabilucius.advancedperks.perks.PerkListCache;
import de.fabilucius.advancedperks.perks.PerkStateController;
import de.fabilucius.advancedperks.perks.PerksConfiguration;
import de.fabilucius.advancedperks.settings.MessageConfiguration;
import de.fabilucius.advancedperks.settings.SettingsConfiguration;
import de.fabilucius.advancedperks.utilities.update.UpdateChecker;
import de.fabilucius.advancedperks.commons.configuration.factory.ConfigSingletonFactory;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdvancedPerks extends JavaPlugin {

    public static final Logger LOGGER = Bukkit.getLogger();

    /* All configuration instances */
    private static SettingsConfiguration settingsConfiguration;
    private static MessageConfiguration messageConfiguration;
    private static PerksConfiguration perksConfiguration;

    /* Perk related repositories and controller's */
    private static PerkDataRepository perkDataRepository;
    private static PerkStateController perkStateController;
    private static PerkListCache perkRegistry;

    /* Telemetry related controller and manager */
    @Nullable
    private static EconomyController economyController;
    private static GuiManager guiManager;

    @Override
    public void onEnable() {
        /* Initialize the configurations first because they don't have any dependencies*/
        settingsConfiguration = ConfigSingletonFactory.createConfiguration(SettingsConfiguration.class);
        perksConfiguration = ConfigSingletonFactory.createConfiguration(PerksConfiguration.class);
        messageConfiguration = ConfigSingletonFactory.createConfiguration(MessageConfiguration.class);

        perkDataRepository = PerkDataRepository.getSingleton();
        perkRegistry = PerkListCache.getSingleton();
        perkStateController = PerkStateController.getSingleton();

        guiManager = GuiManager.getSingleton();
        economyController = EconomyController.getSingleton();
        CompatabilityController.getSingleton();

        new PerksCommand();
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            AdvancedPerksExpansion expansion = new AdvancedPerksExpansion();
            expansion.register();
            LOGGER.log(Level.INFO, "PlaceholderAPI was found and thus the expansion for it was loaded and enabled successfully.");
        }
        Metrics.load();
        UpdateChecker.getSingleton();
    }

    @Override
    public void onDisable() {
        perkStateController.handleShutdown();
        guiManager.handleShutdown();
    }

    public static void reloadPlugin() {
        settingsConfiguration = ConfigSingletonFactory.reloadConfiguration(SettingsConfiguration.class);
        perksConfiguration = ConfigSingletonFactory.reloadConfiguration(PerksConfiguration.class);
        messageConfiguration = ConfigSingletonFactory.reloadConfiguration(MessageConfiguration.class);
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

    public static Optional<EconomyController> getEconomyController() {
        return economyController == null ? Optional.empty() : Optional.of(economyController);
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
}
