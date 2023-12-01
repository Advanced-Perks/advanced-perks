package de.fabilucius.advancedperks.guisystem.perkgui.elements;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.core.guisystem.GuiSound;
import de.fabilucius.advancedperks.core.guisystem.element.AbstractGuiElement;
import de.fabilucius.advancedperks.core.guisystem.element.GuiElement;
import de.fabilucius.advancedperks.core.guisystem.window.GuiWindow;
import de.fabilucius.advancedperks.core.itembuilder.types.ItemStackBuilder;
import de.fabilucius.advancedperks.data.state.PerkStateController;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;

public class DisableAllPerksElement extends AbstractGuiElement {

    @Inject
    private PerkStateController perkStateController;

    public DisableAllPerksElement(GuiWindow guiWindow, String displayName) {
        super(guiWindow, ItemStackBuilder.fromMaterial(Material.REDSTONE_BLOCK)
                .setDisplayName(displayName)
                .build());
    }

    @Override
    public BiConsumer<GuiElement, InventoryClickEvent> handleInventoryClick() {
        return (guiElement, event) -> {
            event.setCancelled(true);
            event.getWhoClicked().closeInventory();
            this.perkStateController.disableAllPerks((Player) event.getWhoClicked());
            this.playSound(GuiSound.SETUP_CLICK);
        };
    }
}
