package de.fabilucius.advancedperks.guisystem.setupperkgui.elements;

import de.fabilucius.advancedperks.core.guisystem.element.AbstractGuiElement;
import de.fabilucius.advancedperks.core.guisystem.element.GuiElement;
import de.fabilucius.advancedperks.core.guisystem.window.GuiWindow;
import de.fabilucius.advancedperks.core.itembuilder.types.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;

public class PerkIconPlaceholderElement extends AbstractGuiElement {

    private final int index;

    public PerkIconPlaceholderElement(GuiWindow guiWindow, int index) {
        super(guiWindow, ItemStackBuilder.fromMaterial(Material.QUARTZ_BLOCK)
                .setDisplayName(ChatColor.DARK_GRAY + "Perk Icon Slot #" + (index + 1))
                .setDescription(ChatColor.GRAY + "Choose a slot where this element should be in the gui.")
                .setAmount(index + 1)
                .build());
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public BiConsumer<GuiElement, InventoryClickEvent> handleInventoryClick() {
        return (guiElement, event) -> { };
    }
}
