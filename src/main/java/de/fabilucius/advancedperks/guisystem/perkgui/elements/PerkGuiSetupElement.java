package de.fabilucius.advancedperks.guisystem.perkgui.elements;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.configuration.ConfigurationLoader;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationInitializationException;
import de.fabilucius.advancedperks.core.guisystem.GuiSystemManager;
import de.fabilucius.advancedperks.core.guisystem.element.AbstractGuiElement;
import de.fabilucius.advancedperks.core.guisystem.element.GuiElement;
import de.fabilucius.advancedperks.core.guisystem.window.GuiWindow;
import de.fabilucius.advancedperks.core.itembuilder.types.ItemStackBuilder;
import de.fabilucius.advancedperks.guisystem.setupperkgui.SetupPerkGuiWindow;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;

public class PerkGuiSetupElement extends AbstractGuiElement {

    @Inject
    private GuiSystemManager guiSystemManager;

    @Inject
    private ConfigurationLoader configurationLoader;

    public PerkGuiSetupElement(GuiWindow guiWindow) {
        super(guiWindow, ItemStackBuilder.fromMaterial(Material.COMMAND_BLOCK)
                .setDisplayName(ChatColor.DARK_GRAY + "Setup layout of the Perk Gui")
                .setDescription(ChatColor.GRAY + "Click here to customize the appearance of the perk gui.")
                .build());
    }

    //TODO cleanup and tidy code
    @Override
    public BiConsumer<GuiElement, InventoryClickEvent> handleInventoryClick() {
        return (guiElement, event) -> {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            try {
                this.guiSystemManager.registerGuiWindowAnOpen(new SetupPerkGuiWindow(this.configurationLoader, player), player);
            } catch (ConfigurationInitializationException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
