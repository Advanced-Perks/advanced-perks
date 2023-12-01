package de.fabilucius.advancedperks.core.guisystem.element;

import de.fabilucius.advancedperks.core.guisystem.GuiSound;
import de.fabilucius.advancedperks.core.guisystem.persistantdata.UuidPersistentDataType;
import de.fabilucius.advancedperks.core.guisystem.window.GuiWindow;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;
import java.util.function.BiConsumer;

public abstract class AbstractGuiElement implements GuiElement {
    private final UUID uniqueId = UUID.randomUUID();
    private final GuiWindow guiWindow;
    private ItemStack icon;

    public AbstractGuiElement(GuiWindow guiWindow, ItemStack icon) {
        this.guiWindow = guiWindow;
        this.icon = icon;
        this.attachUuid(icon);
    }

    public abstract BiConsumer<GuiElement, InventoryClickEvent> handleInventoryClick();

    private void attachUuid(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            throw new IllegalStateException("The gui item %s doesn't return an ItemMeta and thus cannot be identified by an InventoryClickEvent.".formatted(this.getClass().getName()));
        }
        itemMeta.getPersistentDataContainer().set(this.guiWindow.getUuidKey(), new UuidPersistentDataType(), this.uniqueId);
        itemStack.setItemMeta(itemMeta);
    }

    @Override
    public ItemStack getIcon() {
        return icon;
    }

    @Override
    public GuiWindow getGuiWindow() {
        return guiWindow;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public void setIconAndUpdate(ItemStack itemStack) {
        this.attachUuid(itemStack);
        this.icon = itemStack;
        this.guiWindow.getInventory().setItem(this.guiWindow.getSlot(this), itemStack);
    }


    @Override
    public void playSound(GuiSound guiSound) {
        this.guiWindow.playSound(guiSound);
    }
}
