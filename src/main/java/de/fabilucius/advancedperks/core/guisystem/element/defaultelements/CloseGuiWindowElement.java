package de.fabilucius.advancedperks.core.guisystem.element.defaultelements;

import de.fabilucius.advancedperks.core.guisystem.GuiSound;
import de.fabilucius.advancedperks.core.guisystem.element.GuiElement;
import de.fabilucius.advancedperks.core.guisystem.window.GuiWindow;
import de.fabilucius.advancedperks.core.itembuilder.types.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;

public class CloseGuiWindowElement extends AbstractDefaultGuiElement {

    public CloseGuiWindowElement(GuiWindow guiWindow, String displayName) {
        super(guiWindow, ItemStackBuilder.fromMaterial(Material.BARRIER)
                .setDisplayName(displayName)
                .build());
    }

    @Override
    public BiConsumer<GuiElement, InventoryClickEvent> handleInventoryClick() {
        return (guiElement, event) -> {
            event.setCancelled(true);
            event.getWhoClicked().closeInventory();
            this.playSound(GuiSound.OFF_CLICK);
        };
    }
}
