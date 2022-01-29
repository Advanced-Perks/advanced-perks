package de.fabilucius.advancedperks.commons.guisystem;

import com.google.common.collect.Maps;
import de.fabilucius.advancedperks.commons.Initializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class GuiWindow implements InventoryHolder, Initializable {

    private final Inventory inventory;
    private final Player player;
    private int page;
    private final HashMap<Integer, GuiElement> elementLookupTable = Maps.newHashMap();

    public GuiWindow(Inventory inventory, Player player) {
        this.inventory = inventory;
        this.player = player;
        this.initialize();
    }

    public abstract void initialize();

    public final void clearGuiWindow() {
        this.getElementLookupTable().clear();
        this.getInventory().clear();
    }

    public final GuiElement addGuiElement(int slot, GuiElement guiElement) {
        this.getInventory().setItem(slot, guiElement.getItemStack());
        this.getElementLookupTable().put(slot, guiElement);
        return guiElement;
    }

    public final GuiElement getElementBySlot(int slot) {
        return this.getElementLookupTable().get(slot);
    }

    public final int getSlotByElement(GuiElement guiElement) {
        Map.Entry<Integer, GuiElement> matchingEntry = this.getElementLookupTable().entrySet().stream()
                .filter(integerGuiElementEntry -> integerGuiElementEntry.getValue().getUuid().equals(guiElement.getUuid())).findAny().orElse(null);
        return matchingEntry == null ? -1 : matchingEntry.getKey();
    }

    /* the getter and setter of this class */

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public Player getPlayer() {
        return player;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public HashMap<Integer, GuiElement> getElementLookupTable() {
        return elementLookupTable;
    }
}
