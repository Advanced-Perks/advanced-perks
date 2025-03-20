package de.fabilucius.advancedperks.core.gui.manager

import com.google.inject.Inject
import com.google.inject.Singleton
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent

@Singleton
class GuiSystemEventHandler @Inject constructor(
    private val guiSystemManager: GuiSystemManager
) : Listener {

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) =
        guiSystemManager.guis.filter { it.key == event.inventory }
            .flatMap { it.value.elements }
            .forEach { it.function.onInventoryClose(event) }

    @EventHandler
    fun onInventoryOpen(event: InventoryOpenEvent) =
        guiSystemManager.guis.filter { it.key == event.inventory }
            .flatMap { it.value.elements }
            .forEach { it.function.onInventoryOpen(event) }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) =
        event.currentItem?.let { itemStack ->
            guiSystemManager.guis.filter { it.key == event.inventory }
                //.map { it.value.getElementByItemStack(itemStack) }
                //.forEach { it.function.onInventoryClick(event) }
        }

}