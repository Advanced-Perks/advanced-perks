package de.fabilucius.advancedperks.core.guisystem.element.defaultelements;

import de.fabilucius.advancedperks.core.guisystem.element.AbstractGuiElement;
import de.fabilucius.advancedperks.core.guisystem.window.GuiWindow;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractDefaultGuiElement extends AbstractGuiElement {
    protected AbstractDefaultGuiElement(GuiWindow guiWindow, ItemStack icon) {
        super(guiWindow, icon);
    }
}
