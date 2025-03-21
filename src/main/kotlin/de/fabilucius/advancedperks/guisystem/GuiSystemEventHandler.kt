package de.fabilucius.advancedperks.guisystem

import com.google.inject.Inject
import com.google.inject.Singleton
import de.fabilucius.advancedperks.AdvancedPerks
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

@Singleton
class GuiSystemEventHandler @Inject constructor(
    plugin: AdvancedPerks,
    private val guiManager: GuiSystemManager,
) : Listener {

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val guiId = (event.inventory.holder as? PerkGui)?.id
        val gui = guiManager.managedGuis[guiId] ?: return
        event.isCancelled = true
        val element = gui.perkGuiElements[event.slot] ?: return
        element.onInventoryClick(event)
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val guiId = (event.inventory.holder as? PerkGui)?.id
        if(event.inventory.viewers.size >= 1){
            guiManager.managedGuis.remove(guiId)
        }
    }

}