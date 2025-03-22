package de.fabilucius.advancedperks.guisystem

import com.google.inject.Inject
import com.google.inject.Singleton
import de.fabilucius.advancedperks.AdvancedPerks
import de.fabilucius.advancedperks.event.perk.PerkToggleEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

@Singleton
class GuiSystemEventHandler @Inject constructor(
    private val plugin: AdvancedPerks,
    private val guiManager: GuiSystemManager,
) : Listener {

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val guiId = (event.inventory.holder as? PerkGui)?.player?.uniqueId
        val gui = guiManager.managedGuis[guiId] ?: return
        event.isCancelled = true
        val element = gui.perkGuiElements[event.slot] ?: return
        element.onInventoryClick(event)
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val guiId = (event.inventory.holder as? PerkGui)?.player?.uniqueId
        if (event.inventory.viewers.size >= 1) {
            guiManager.managedGuis.remove(guiId)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPerkToggle(event: PerkToggleEvent) {
        if (event.isCancelled) {
            return
        }
        val gui = guiManager.managedGuis[event.player.uniqueId] ?: return
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            gui.refreshPerkGui()
        }, 1L)
    }

}