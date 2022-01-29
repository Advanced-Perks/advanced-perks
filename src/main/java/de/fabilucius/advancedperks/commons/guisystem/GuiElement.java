package de.fabilucius.advancedperks.commons.guisystem;

import de.fabilucius.advancedperks.commons.guisystem.annotation.CancelInventoryInteraction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.function.BiConsumer;

public class GuiElement {

    private final UUID uuid = UUID.randomUUID();
    private final GuiWindow guiWindow;
    private final BiConsumer<GuiElement, InventoryClickEvent> inventoryClickConsumer;
    private ItemStack itemStack;

    public GuiElement(GuiWindow guiWindow, BiConsumer<GuiElement, InventoryClickEvent> inventoryClickConsumer, ItemStack itemStack) {
        this.guiWindow = guiWindow;
        this.inventoryClickConsumer = inventoryClickConsumer;
        this.itemStack = itemStack;
    }

    public final void acceptInventoryClick(InventoryClickEvent event) {
        this.getInventoryClickConsumer().accept(this, event);
    }

    public boolean shouldCancelInventoryInteraction() {
        return this.getClass().isAnnotationPresent(CancelInventoryInteraction.class);
    }

    /* the getter and setter of this class */

    public UUID getUuid() {
        return uuid;
    }

    public GuiWindow getGuiWindow() {
        return guiWindow;
    }

    public BiConsumer<GuiElement, InventoryClickEvent> getInventoryClickConsumer() {
        return inventoryClickConsumer;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.getGuiWindow().getInventory().setItem(this.getGuiWindow().getSlotByElement(this), itemStack);
        this.itemStack = itemStack;
    }
}
