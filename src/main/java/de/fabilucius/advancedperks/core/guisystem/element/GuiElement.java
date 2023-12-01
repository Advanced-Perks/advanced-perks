package de.fabilucius.advancedperks.core.guisystem.element;

import de.fabilucius.advancedperks.core.guisystem.GuiSound;
import de.fabilucius.advancedperks.core.guisystem.window.GuiWindow;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.function.BiConsumer;

public interface GuiElement {

    GuiWindow getGuiWindow();

    ItemStack getIcon();

    BiConsumer<GuiElement, InventoryClickEvent> handleInventoryClick();

    void setIconAndUpdate(ItemStack itemStack);

    UUID getUniqueId();

    void playSound(GuiSound guiSound);

}
