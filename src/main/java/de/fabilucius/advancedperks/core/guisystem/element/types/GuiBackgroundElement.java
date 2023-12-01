package de.fabilucius.advancedperks.core.guisystem.element.types;

import de.fabilucius.advancedperks.core.guisystem.element.AbstractGuiElement;
import de.fabilucius.advancedperks.core.guisystem.element.GuiElement;
import de.fabilucius.advancedperks.core.guisystem.window.GuiWindow;
import de.fabilucius.advancedperks.core.itembuilder.types.ItemStackBuilder;
import de.fabilucius.advancedperks.guisystem.setupperkgui.SetupPerkGuiWindow;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;

public class GuiBackgroundElement extends AbstractGuiElement {

    public GuiBackgroundElement(GuiWindow guiWindow) {
        super(guiWindow, ItemStackBuilder.fromMaterial(Material.BLACK_STAINED_GLASS_PANE)
                .setDisplayName(" ")
                .build());
    }

    @Override
    public BiConsumer<GuiElement, InventoryClickEvent> handleInventoryClick() {
        return ((guiElement, event) -> event.setCancelled(!this.getGuiWindow().getClass().equals(SetupPerkGuiWindow.class)));
    }
}
