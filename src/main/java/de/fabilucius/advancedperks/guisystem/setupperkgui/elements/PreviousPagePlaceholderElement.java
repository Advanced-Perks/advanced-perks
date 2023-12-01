package de.fabilucius.advancedperks.guisystem.setupperkgui.elements;

import de.fabilucius.advancedperks.core.guisystem.HeadTexture;
import de.fabilucius.advancedperks.core.guisystem.element.AbstractGuiElement;
import de.fabilucius.advancedperks.core.guisystem.element.GuiElement;
import de.fabilucius.advancedperks.core.guisystem.window.GuiWindow;
import de.fabilucius.advancedperks.core.itembuilder.types.SkullStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;

public class PreviousPagePlaceholderElement extends AbstractGuiElement {
    public PreviousPagePlaceholderElement(GuiWindow guiWindow) {
        super(guiWindow, SkullStackBuilder.fromMaterial(Material.PLAYER_HEAD)
                .setBase64Value(HeadTexture.ARROW_LEFT.getValue())
                .setDisplayName(ChatColor.DARK_GRAY + "Previous Page Slot")
                .setDescription(ChatColor.GRAY + "Choose a slot where this element should be in the gui.")
                .build());
    }

    @Override
    public BiConsumer<GuiElement, InventoryClickEvent> handleInventoryClick() {
        return (guiElement, event) -> {};
    }
}
