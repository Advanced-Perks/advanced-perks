package de.fabilucius.advancedperks.guisystem.setupperkgui.elements;

import de.fabilucius.advancedperks.core.guisystem.element.AbstractGuiElement;
import de.fabilucius.advancedperks.core.guisystem.element.GuiElement;
import de.fabilucius.advancedperks.core.guisystem.window.GuiWindow;
import de.fabilucius.advancedperks.core.itembuilder.types.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;

public class PerkTogglePlaceholderElement extends AbstractGuiElement {

    private final int index;

    public PerkTogglePlaceholderElement(GuiWindow guiWindow, int index) {
        super(guiWindow, ItemStackBuilder.fromMaterial(Material.GRAY_DYE)
                .setDisplayName(ChatColor.DARK_GRAY + "Perk Toggle Slot #" + (index + 1))
                .setDescription(ChatColor.GRAY + "Choose a slot where this element should be in the gui.")
                .setAmount(index + 1)
                .build());
        this.index = index;
    }

    @Override
    public BiConsumer<GuiElement, InventoryClickEvent> handleInventoryClick() {
        return (guiElement, event) -> {};
    }

    public int getIndex() {
        return index;
    }
}
