package de.fabilucius.advancedperks.gui.elements;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.guisystem.GuiElement;
import de.fabilucius.advancedperks.commons.guisystem.GuiWindow;
import de.fabilucius.advancedperks.commons.guisystem.annotation.CancelInventoryInteraction;
import de.fabilucius.advancedperks.utilities.MessageConfigReceiver;
import de.fabilucius.sympel.item.builder.types.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@CancelInventoryInteraction
public class DisableAllPerksElement extends GuiElement {
    public DisableAllPerksElement(GuiWindow guiWindow) {
        super(guiWindow, (guiElement, event) -> {
            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                AdvancedPerks.getInstance().getPerkStateController().disableAllPerks(player);
            }
        }, ItemStackBuilder.fromMaterial(Material.REDSTONE_BLOCK)
                .setDisplayName(MessageConfigReceiver.getMessage("Gui.Disable-All-Perks.Name"))
                .setDescription(MessageConfigReceiver.getMessageList("Gui.Disable-All-Perks.Description"))
                .build());
    }
}
