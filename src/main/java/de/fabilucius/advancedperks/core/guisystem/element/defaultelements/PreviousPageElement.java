package de.fabilucius.advancedperks.core.guisystem.element.defaultelements;

import de.fabilucius.advancedperks.core.guisystem.GuiSound;
import de.fabilucius.advancedperks.core.guisystem.HeadTexture;
import de.fabilucius.advancedperks.core.guisystem.element.GuiElement;
import de.fabilucius.advancedperks.core.guisystem.window.types.AbstractPageGuiWindow;
import de.fabilucius.advancedperks.core.itembuilder.types.skull.SkullStackBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;

public class PreviousPageElement extends AbstractDefaultGuiElement {

    private final AbstractPageGuiWindow abstractPageGuiWindow;

    public PreviousPageElement(AbstractPageGuiWindow guiWindow, String displayName) {
        super(guiWindow, SkullStackBuilder.fromMaterial(Material.PLAYER_HEAD)
                .setBase64Value(HeadTexture.ARROW_LEFT.getValue())
                .setDisplayName(displayName)
                .build());
        this.abstractPageGuiWindow = guiWindow;
    }

    @Override
    public BiConsumer<GuiElement, InventoryClickEvent> handleInventoryClick() {
        return (guiElement, event) -> {
            event.setCancelled(true);
            this.abstractPageGuiWindow.previousPage();
            this.playSound(GuiSound.NORMAL_CLICK);
        };
    }
}
