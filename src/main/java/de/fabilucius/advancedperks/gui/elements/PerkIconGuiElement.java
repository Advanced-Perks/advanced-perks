package de.fabilucius.advancedperks.gui.elements;

import de.fabilucius.advancedperks.commons.guisystem.GuiElement;
import de.fabilucius.advancedperks.commons.guisystem.GuiWindow;
import de.fabilucius.advancedperks.commons.guisystem.annotation.CancelInventoryInteraction;
import de.fabilucius.advancedperks.perks.Perk;

@CancelInventoryInteraction
public class PerkIconGuiElement extends GuiElement {

    private final Perk perk;

    public PerkIconGuiElement(GuiWindow guiWindow, Perk perk) {
        super(guiWindow, (guiElement, event) -> {
        }, perk.getIcon());
        this.perk = perk;
    }

    /* the getter and setter of this class */

    public Perk getPerk() {
        return perk;
    }
}
