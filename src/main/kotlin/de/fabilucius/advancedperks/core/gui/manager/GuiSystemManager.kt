package de.fabilucius.advancedperks.core.gui.manager

import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.Singleton
import de.fabilucius.advancedperks.AdvancedPerks
import de.fabilucius.advancedperks.core.gui.GuiWindow
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory

@Singleton
class GuiSystemManager @Inject constructor(
    injector: Injector,
    advancedPerks: AdvancedPerks
) {

    init {
        Bukkit.getPluginManager().registerEvents(injector.getInstance(GuiSystemEventHandler::class.java), advancedPerks)
    }

    val guis = mutableMapOf<Inventory, GuiWindow>()

}