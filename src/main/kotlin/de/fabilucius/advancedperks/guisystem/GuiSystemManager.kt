package de.fabilucius.advancedperks.guisystem

import com.google.inject.Inject
import com.google.inject.Singleton
import org.bukkit.entity.Player
import java.util.UUID

@Singleton
class GuiSystemManager @Inject constructor(
    private val perkGuiFactory: PerkGuiFactory,
    private val guiSelectionService: GuiSelectionService,
) {

    val managedGuis: MutableMap<UUID, PerkGui> = mutableMapOf()

    //TODO in the future add a way to open the perk gui of another player for admin reasons
    fun openGui(player: Player) {
        if (managedGuis.containsKey(player.uniqueId)) {
            return
        }
        val perkGui = perkGuiFactory.create(guiSelectionService.activeGuiBlueprint, player)
        managedGuis[perkGui.player.uniqueId] = perkGui
        player.openInventory(perkGui.perkInventory)
    }

}