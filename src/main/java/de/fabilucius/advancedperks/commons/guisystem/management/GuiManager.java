package de.fabilucius.advancedperks.commons.guisystem.management;

import com.google.common.collect.Maps;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.guisystem.GuiElement;
import de.fabilucius.advancedperks.commons.guisystem.GuiWindow;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class GuiManager implements Listener {

    private static GuiManager instance;

    /* Enforce a singleton to prevent multiple listener registrations */
    public static GuiManager getSingleton() {
        if (instance == null) {
            instance = new GuiManager();
        }
        return instance;
    }

    private final HashMap<GuiWindow, Player> openedGuis = Maps.newHashMap();

    private GuiManager() {
        Bukkit.getPluginManager().registerEvents(this, AdvancedPerks.getInstance());
    }

    public void handleShutdown() {
        this.getOpenedGuis().values().forEach(Player::closeInventory);
        this.getOpenedGuis().clear();
    }

    public final void openGui(Player player, GuiWindow guiWindow) {
        this.getOpenedGuis().put(guiWindow, player);
        player.openInventory(guiWindow.getInventory());
    }

    public final GuiWindow getGuiWindowByInventory(Inventory inventory) {
        return this.getOpenedGuis().keySet().stream().filter(guiWindow ->
                guiWindow.getInventory().equals(inventory)).findAny().orElse(null);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClick().isShiftClick() ? event.getInventory() : event.getClickedInventory();
        GuiWindow guiWindow = this.getGuiWindowByInventory(inventory);
        if (guiWindow != null) {
            if (inventory != null && event.getCurrentItem() != null) {
                GuiElement guiElement = guiWindow.getElementBySlot(event.getSlot());
                if (guiElement != null) {
                    guiElement.acceptInventoryClick(event);
                    event.setCancelled(guiElement.shouldCancelInventoryInteraction());
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        GuiWindow guiWindow = this.getGuiWindowByInventory(event.getInventory());
        if (guiWindow != null && event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            this.getOpenedGuis().remove(guiWindow, player);
        }
    }

    /* the getter and setter of this class */

    public HashMap<GuiWindow, Player> getOpenedGuis() {
        return openedGuis;
    }
}
