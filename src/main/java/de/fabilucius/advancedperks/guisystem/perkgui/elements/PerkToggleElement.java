package de.fabilucius.advancedperks.guisystem.perkgui.elements;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.core.guisystem.element.AbstractGuiElement;
import de.fabilucius.advancedperks.core.guisystem.element.GuiElement;
import de.fabilucius.advancedperks.core.guisystem.window.GuiWindow;
import de.fabilucius.advancedperks.core.itembuilder.types.ItemStackBuilder;
import de.fabilucius.advancedperks.data.state.PerkStateController;
import de.fabilucius.advancedperks.perk.Perk;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;

public class PerkToggleElement extends AbstractGuiElement {

    @Inject
    private PerkStateController perkStateController;

    private final Perk perk;
    private final String enabledText;
    private final String disabledText;

    public PerkToggleElement(GuiWindow guiWindow, Perk perk, boolean enabled, String enabledText, String disabledText) {
        super(guiWindow, ItemStackBuilder.fromMaterial(enabled ? Material.LIME_DYE : Material.GRAY_DYE)
                .setDisplayName(enabled ? enabledText : disabledText)
                .build());
        this.perk = perk;
        this.enabledText = enabledText;
        this.disabledText = disabledText;
    }

    @Override
    public BiConsumer<GuiElement, InventoryClickEvent> handleInventoryClick() {
        return ((guiElement, event) -> {
            event.setCancelled(true);
            switch (this.perkStateController.togglePerk(this.getGuiWindow().getPlayer(), this.perk)) {
                case ENABLED -> this.setIconAndUpdate(ItemStackBuilder.fromMaterial(Material.LIME_DYE)
                        .setDisplayName(this.enabledText)
                        .build());
                case DISABLED -> this.setIconAndUpdate(ItemStackBuilder.fromMaterial(Material.GRAY_DYE)
                        .setDisplayName(this.disabledText)
                        .build());
            }
        });
    }
}
