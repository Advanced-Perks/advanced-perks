package de.fabilucius.advancedperks.guisystem.perkgui.elements;

import de.fabilucius.advancedperks.core.guisystem.element.AbstractGuiElement;
import de.fabilucius.advancedperks.core.guisystem.element.GuiElement;
import de.fabilucius.advancedperks.core.guisystem.window.GuiWindow;
import de.fabilucius.advancedperks.core.itembuilder.types.ItemStackBuilder;
import de.fabilucius.advancedperks.perk.Perk;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;

public class PerkIconElement extends AbstractGuiElement {
    public PerkIconElement(GuiWindow guiWindow, Perk perk) {
        super(guiWindow, ItemStackBuilder.fromMaterial(/* TODO get material from perkgui icon stuff*/Material.DOLPHIN_SPAWN_EGG)
                .setDisplayName(perk.getDisplayName())
                .setDescription(perk.getDescription().lines())
                .build());
    }

    @Override
    public BiConsumer<GuiElement, InventoryClickEvent> handleInventoryClick() {
        return (guiElement, event) -> event.setCancelled(true);
    }
}
