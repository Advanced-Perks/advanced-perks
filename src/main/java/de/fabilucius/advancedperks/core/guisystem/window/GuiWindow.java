package de.fabilucius.advancedperks.core.guisystem.window;

import de.fabilucius.advancedperks.core.guisystem.GuiSound;
import de.fabilucius.advancedperks.core.guisystem.element.GuiElement;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface GuiWindow {

    Map<UUID, GuiElement> getGuiElements();

    Inventory getInventory();

    void initializeGui();

    void clearGui();

    void addGuiElement(GuiElement guiElement, int slot);

    void removeGuiElement(GuiElement guiElement);

    Optional<GuiElement> getGuiElementByItemStack(ItemStack itemStack);

    int getSlot(GuiElement guiElement);

    NamespacedKey getUuidKey();

    void setTitle(String title);

    Player getPlayer();

    void playSound(GuiSound guiSound);

}
