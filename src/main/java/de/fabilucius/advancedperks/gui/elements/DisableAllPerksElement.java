package de.fabilucius.advancedperks.gui.elements;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.guisystem.GuiElement;
import de.fabilucius.advancedperks.commons.guisystem.GuiWindow;
import de.fabilucius.advancedperks.commons.guisystem.annotation.CancelInventoryInteraction;
import de.fabilucius.sympel.item.builder.types.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@CancelInventoryInteraction
public class DisableAllPerksElement extends GuiElement {
    public DisableAllPerksElement(GuiWindow guiWindow) {
        super(guiWindow, (guiElement, event) -> {
            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                AdvancedPerks.getPerkStateController().disableAllPerks(player);
            }
        }, ItemStackBuilder.fromMaterial(Material.REDSTONE_BLOCK)
                .setDisplayName(AdvancedPerks.getMessageConfiguration().getMessage("Gui.Disable-All-Perks.Name"))
                .setDescription(AdvancedPerks.getMessageConfiguration().getMessageList("Gui.Disable-All-Perks.Description"))
                .build());
    }
}
