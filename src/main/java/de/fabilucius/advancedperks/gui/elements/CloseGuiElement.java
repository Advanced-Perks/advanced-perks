package de.fabilucius.advancedperks.gui.elements;

import de.fabilucius.advancedperks.commons.guisystem.GuiElement;
import de.fabilucius.advancedperks.commons.guisystem.GuiWindow;
import de.fabilucius.advancedperks.commons.guisystem.annotation.CancelInventoryInteraction;
import de.fabilucius.advancedperks.utilities.ItemStackBuilder;
import de.fabilucius.advancedperks.utilities.MessageConfigReceiver;
import org.bukkit.Material;

@CancelInventoryInteraction
public class CloseGuiElement extends GuiElement {
    public CloseGuiElement(GuiWindow guiWindow) {
        super(guiWindow, (guiElement, event) -> {
            event.getWhoClicked().closeInventory();
        }, new ItemStackBuilder(Material.BARRIER)
                .setDisplayName(MessageConfigReceiver.getMessage("Gui.Close-Gui.Name"))
                .setDescription(MessageConfigReceiver.getMessageList("Gui.Close-Gui.Description"))
                .build());
    }
}
