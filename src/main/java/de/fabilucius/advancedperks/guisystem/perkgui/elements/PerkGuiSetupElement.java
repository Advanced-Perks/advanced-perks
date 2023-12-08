package de.fabilucius.advancedperks.guisystem.perkgui.elements;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.core.configuration.ConfigurationLoader;
import de.fabilucius.advancedperks.core.configuration.exception.ConfigurationInitializationException;
import de.fabilucius.advancedperks.core.SettingsConfiguration;
import de.fabilucius.advancedperks.core.guisystem.GuiSound;
import de.fabilucius.advancedperks.core.guisystem.GuiSystemManager;
import de.fabilucius.advancedperks.core.guisystem.element.AbstractGuiElement;
import de.fabilucius.advancedperks.core.guisystem.element.GuiElement;
import de.fabilucius.advancedperks.core.guisystem.window.GuiWindow;
import de.fabilucius.advancedperks.core.itembuilder.types.ItemStackBuilder;
import de.fabilucius.advancedperks.core.logging.APLogger;
import de.fabilucius.advancedperks.guisystem.setupperkgui.SetupPerkGuiWindow;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;
import java.util.logging.Level;

public class PerkGuiSetupElement extends AbstractGuiElement {

    @Inject
    private GuiSystemManager guiSystemManager;

    @Inject
    private ConfigurationLoader configurationLoader;

    @Inject
    private APLogger logger;

    public PerkGuiSetupElement(GuiWindow guiWindow) {
        super(guiWindow, ItemStackBuilder.fromMaterial(Material.COMMAND_BLOCK)
                .setDisplayName(ChatColor.DARK_GRAY + "Setup layout of the Perk Gui")
                .setDescription(ChatColor.GRAY + "Click here to customize the appearance of the perk gui.")
                .build());
    }

    @Override
    public BiConsumer<GuiElement, InventoryClickEvent> handleInventoryClick() {
        return (guiElement, event) -> {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            try {
                SettingsConfiguration settingsConfiguration = this.configurationLoader.getConfigurationAndLoad(SettingsConfiguration.class);
                this.guiSystemManager.registerGuiWindowAnOpen(new SetupPerkGuiWindow(this.configurationLoader, player, settingsConfiguration.isGuiClickSoundsEnabled()), player);
                this.playSound(GuiSound.SETUP_CLICK);
            } catch (ConfigurationInitializationException e) {
                player.sendMessage("&cAn error occurred while loading a configuration needed for the setup gui check the console for additional information.");
                this.logger.log(Level.SEVERE, "A configuration related error occurred during initialization of the SetupPerkGuiWindow.", e);
            }
        };
    }
}
