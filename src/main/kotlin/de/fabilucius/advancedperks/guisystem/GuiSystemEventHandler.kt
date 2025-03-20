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
        val element = gui.perkGuiElements[event.slot] ?: return
        element.onInventoryClick(event)
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val guiId = (event.inventory.holder as? PerkGui)?.id
        val gui = guiManager.managedGuis[guiId] ?: return
        Bukkit.broadcastMessage(gui.inventory.viewers.size.toString())
        //TODO add check when you know if its pre or post close
        guiManager.managedGuis.remove(guiId)
    }

}