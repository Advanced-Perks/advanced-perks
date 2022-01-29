package de.fabilucius.advancedperks.gui.elements;

import de.fabilucius.advancedperks.commons.guisystem.GuiElement;
import de.fabilucius.advancedperks.commons.guisystem.GuiWindow;
import de.fabilucius.advancedperks.commons.guisystem.annotation.CancelInventoryInteraction;
import de.fabilucius.advancedperks.utilities.MessageConfigReceiver;
import de.fabilucius.sympel.item.builder.types.SkullStackBuilder;

@CancelInventoryInteraction
public class PreviousPageGuiElement extends GuiElement {
    public PreviousPageGuiElement(GuiWindow guiWindow) {
        super(guiWindow, (guiElement, event) -> {
            guiWindow.setPage(Math.max(0, guiWindow.getPage() - 1));
            guiWindow.initialize();
        }, SkullStackBuilder.fromApproximateMaterial("PLAYER_HEAD")
                .setBase64Value("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzdhZWU5YTc1YmYwZGY3ODk3MTgzMDE1Y2NhMGIyYTdkNzU1YzYzMzg4ZmYwMTc1MmQ1ZjQ0MTlmYzY0NSJ9fX0=")
                .setDisplayName(MessageConfigReceiver.getMessage("Gui.Previous-Page.Name"))
                .setDescription(MessageConfigReceiver.getMessageList("Gui.Previous-Page.Description"))
                .build());
    }
}
