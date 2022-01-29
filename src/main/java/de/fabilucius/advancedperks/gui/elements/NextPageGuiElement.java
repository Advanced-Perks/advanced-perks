package de.fabilucius.advancedperks.gui.elements;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.guisystem.GuiElement;
import de.fabilucius.advancedperks.commons.guisystem.GuiWindow;
import de.fabilucius.advancedperks.commons.guisystem.annotation.CancelInventoryInteraction;
import de.fabilucius.advancedperks.gui.PerkGuiWindow;
import de.fabilucius.advancedperks.utilities.ItemStackBuilder;
import de.fabilucius.advancedperks.utilities.MessageConfigReceiver;
import org.bukkit.Material;

@CancelInventoryInteraction
public class NextPageGuiElement extends GuiElement {
    public NextPageGuiElement(GuiWindow guiWindow) {
        super(guiWindow, (guiElement, event) -> {
            guiWindow.setPage(Math.min(AdvancedPerks.getInstance().getPerkRegistry()
                    .getPerks().size() / PerkGuiWindow.PERKS_PER_PAGE, guiWindow.getPage() + 1));
            guiWindow.initialize();
        }, new ItemStackBuilder(Material.AIR)
                .setHeadBase64Value("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgyYWQxYjljYjRkZDIxMjU5YzBkNzVhYTMxNWZmMzg5YzNjZWY3NTJiZTM5NDkzMzgxNjRiYWM4NGE5NmUifX19")
                .setDisplayName(MessageConfigReceiver.getMessage("Gui.Next-Page.Name"))
                .setDescription(MessageConfigReceiver.getMessageList("Gui.Next-Page.Description"))
                .build());
    }
}
