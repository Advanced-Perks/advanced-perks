package de.fabilucius.advancedperks.core.guisystem;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Injector;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.core.guisystem.element.GuiElement;
import de.fabilucius.advancedperks.core.guisystem.window.GuiWindow;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Optional;

public class GuiSystemManager implements Listener {

    @Inject
    private Injector injector;

    private final HashMap<Inventory, GuiWindow> guiWindows = Maps.newHashMap();

    @Inject
    public GuiSystemManager(AdvancedPerks advancedPerks) {
        Bukkit.getPluginManager().registerEvents(this, advancedPerks);
    }

    public GuiWindow registerGuiWindowAnOpen(GuiWindow guiWindow, Player player) {
        this.injector.injectMembers(guiWindow);
        this.guiWindows.put(guiWindow.getInventory(), guiWindow);
        guiWindow.initializeGui();
        player.openInventory(guiWindow.getInventory());
        return guiWindow;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!this.guiWindows.containsKey(event.getClickedInventory()) || event.getCurrentItem() == null) {
            return;
        }
        //TODO check if shiftclick needs exception
        GuiWindow guiWindow = this.guiWindows.get(event.getClickedInventory());
        if (guiWindow == null) {
            return;
        }
        Optional<GuiElement> potentialGuiElement = guiWindow.getGuiElementByItemStack(event.getCurrentItem());
        if (potentialGuiElement.isEmpty()) {
            return;
        }
        potentialGuiElement.get().handleInventoryClick().accept(potentialGuiElement.get(), event);
    }

}
