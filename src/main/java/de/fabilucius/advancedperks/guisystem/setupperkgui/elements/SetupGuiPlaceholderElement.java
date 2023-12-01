package de.fabilucius.advancedperks.guisystem.setupperkgui.elements;

import de.fabilucius.advancedperks.core.guisystem.GuiSound;
import de.fabilucius.advancedperks.core.guisystem.element.AbstractGuiElement;
import de.fabilucius.advancedperks.core.guisystem.element.GuiElement;
import de.fabilucius.advancedperks.core.guisystem.window.GuiWindow;
import de.fabilucius.advancedperks.core.itembuilder.types.ItemStackBuilder;
import de.fabilucius.advancedperks.guisystem.configuration.PerkGuiSaveResult;
import de.fabilucius.advancedperks.guisystem.setupperkgui.SetupPerkGuiWindow;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;

public class SetupGuiPlaceholderElement extends AbstractGuiElement {
    public SetupGuiPlaceholderElement(GuiWindow guiWindow) {
        super(guiWindow, ItemStackBuilder.fromMaterial(Material.CHAIN_COMMAND_BLOCK)
                .setDisplayName(ChatColor.DARK_GRAY + "Setup Gui Slot")
                .setDescription(ChatColor.GRAY + "Choose a slot where this element should be in the gui.",
                        ChatColor.GRAY + "Press " + ChatColor.AQUA + "Control + Q " + ChatColor.GRAY + "to save the changes done to the layout.",
                        ChatColor.GRAY + "Press " + ChatColor.AQUA + "Q " + ChatColor.GRAY + "to enable/disable a background for the gui.")
                .build());
    }

    //TODO currently unneeded make it impossible to drop it out of the inventory or move it into your own gui (for all placeholder elements)
    @Override
    public BiConsumer<GuiElement, InventoryClickEvent> handleInventoryClick() {
        return (guiElement, event) -> {
            SetupPerkGuiWindow guiWindow = (SetupPerkGuiWindow) this.getGuiWindow();
            if (event.getClick().equals(ClickType.DROP)) {
                guiWindow.toggleHasBackground();
                event.setCancelled(true);
                this.playSound(GuiSound.NORMAL_CLICK);
            } else if (event.getClick().equals(ClickType.CONTROL_DROP)) {
                event.setCancelled(true);
                PerkGuiSaveResult result = guiWindow.save();
                if (result.equals(PerkGuiSaveResult.SUCCESS)) {
                    event.getWhoClicked().closeInventory();
                    this.playSound(GuiSound.SETUP_CLICK);
                } else {
                    this.getGuiWindow().setTitle(result.getMessage());
                    this.playSound(GuiSound.ERROR_CLICK);
                }
            }
        };
    }
}
