package de.fabilucius.advancedperks.core.guisystem.element.types;

import de.fabilucius.advancedperks.core.guisystem.HeadTexture;
import de.fabilucius.advancedperks.core.guisystem.element.AbstractGuiElement;
import de.fabilucius.advancedperks.core.guisystem.element.GuiElement;
import de.fabilucius.advancedperks.core.guisystem.window.types.AbstractPageGuiWindow;
import de.fabilucius.advancedperks.core.itembuilder.types.SkullStackBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;

public class NextPageElement extends AbstractGuiElement {

    private final AbstractPageGuiWindow abstractPageGuiWindow;
    private final int maxPage;

    public NextPageElement(AbstractPageGuiWindow guiWindow, String displayName, int maxPage) {
        super(guiWindow, SkullStackBuilder.fromMaterial(Material.PLAYER_HEAD)
                .setBase64Value(HeadTexture.ARROW_RIGHT.getValue())
                .setDisplayName(displayName)
                .build());
        this.abstractPageGuiWindow = guiWindow;
        this.maxPage = maxPage;
    }

    @Override
    public BiConsumer<GuiElement, InventoryClickEvent> handleInventoryClick() {
        return (guiElement, event) -> {
            event.setCancelled(true);
            if (this.abstractPageGuiWindow.getPage() == this.maxPage) {
                return;
            }
            this.abstractPageGuiWindow.nextPage();
        };
    }
}
