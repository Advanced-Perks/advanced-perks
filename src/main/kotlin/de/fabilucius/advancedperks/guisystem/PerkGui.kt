package de.fabilucius.advancedperks.guisystem

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import de.fabilucius.advancedperks.core.functionextensions.translateColorCodes
import de.fabilucius.advancedperks.data.PerkDataRepository
import de.fabilucius.advancedperks.data.state.PerkStateController
import de.fabilucius.advancedperks.guisystem.blueprint.GuiBlueprint
import de.fabilucius.advancedperks.guisystem.elements.*
import de.fabilucius.advancedperks.registry.PerkRegistry
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import java.util.*

class PerkGui @Inject constructor(
    private val perkStateController: PerkStateController,
    private val perkRegistry: PerkRegistry,
    private val perkDataRepository: PerkDataRepository,
    @Assisted private val blueprint: GuiBlueprint,
    @Assisted private val player: Player,
) : InventoryHolder {

    var perkInventory: Inventory = Bukkit.createInventory(this, blueprint.size, blueprint.title.translateColorCodes())
    private var page: Int = 0
    val perkGuiElements: MutableMap<Int, PerkGuiElement> = mutableMapOf()
    val id: UUID = UUID.randomUUID()

    init {
        constructPerkGui()
    }

    fun previousPage() {
        if (page > 0) {
            page--
            constructPerkGui()
        }
    }

    fun nextPage() {
        val perksPerkPage = blueprint.perkButton.inventorySlots.size
        val nextPageAvailable = perkRegistry.perks.size > perksPerkPage.times(page).plus(perksPerkPage)
        if (nextPageAvailable) {
            page++
            constructPerkGui()
        }
    }

    private fun addPerkGuiElement(slot: Int, element: PerkGuiElement) = perkGuiElements.put(slot, element).also {
        perkInventory.setItem(slot, element.getGuiIcon())
    }

    private fun constructPerkGui() {
        perkInventory.clear()
        perkGuiElements.clear()
        constructBackground()
        constructPerkAndToggleButtons()
        constructPaginationButtons()
        constructUtilityButtons()
    }

    private fun constructBackground() {
        val blueprint = blueprint.background
        if (blueprint.enabled) {
            for (index in 0..perkInventory.size.minus(1)) {
                perkInventory.setItem(index, blueprint.representation?.toItemStack())
            }
        }
    }

    private fun constructPerkAndToggleButtons() {
        val blueprint = blueprint.perkButton
        val perksPerkPage = blueprint.inventorySlots.size
        val perks = perkRegistry.perks
        val minIndex = perksPerkPage.times(page).coerceAtLeast(0)
        val maxIndex = minIndex.plus(perksPerkPage).coerceAtMost(perks.size)
        val perksForPage = perks.subList(minIndex, maxIndex)
        if (perksForPage.isEmpty()) {
            //TODO some sort of error as this shouldnt happen
            return
        }
        val perkData = perkDataRepository.getPerkDataByPlayer(player)

        blueprint.inventorySlots.forEachIndexed { index, slot ->
            if (index < perksForPage.size) {
                val perk = perksForPage[index]
                addPerkGuiElement(slot, PerkButtonGuiElement(perk))
            }
        }

        if (blueprint.toggleButton.enabled) {
            blueprint.toggleButton.inventorySlots?.forEachIndexed { index, slot ->
                if (index < perksForPage.size) {
                    val perk = perksForPage[index]
                    val activeIcon = blueprint.toggleButton.activeRepresentation?.toItemStack(
                        mapOf(
                            "identifier" to ChatColor.stripColor(perk.displayName)!!
                        )
                    )
                    val inactiveIcon = blueprint.toggleButton.inactiveRepresentation?.toItemStack(
                        mapOf(
                            "identifier" to ChatColor.stripColor(perk.displayName)!!
                        )
                    )
                    addPerkGuiElement(
                        slot,
                        ToggleButtonGuiElement(activeIcon!!, inactiveIcon!!, perk, perkData, perkStateController, this)
                    )
                }
            }
        }
    }

    private fun constructPaginationButtons() {
        val perksPerkPage = blueprint.perkButton.inventorySlots.size
        val blueprint = blueprint.paginationButton
        val previousPageAvailable = page > 0
        val nextPageAvailable = perkRegistry.perks.size > perksPerkPage.times(page).plus(perksPerkPage)

        if (previousPageAvailable) {
            addPerkGuiElement(
                blueprint.previousPageButton.slot,
                PreviousPageButtonGuiElement(this, blueprint.previousPageButton.representation.toItemStack())
            )
        }

        if (nextPageAvailable) {
            addPerkGuiElement(
                blueprint.nextPageButton.slot,
                NextPageButtonGuiElement(this, blueprint.nextPageButton.representation.toItemStack())
            )
        }
    }

    private fun constructUtilityButtons() {
        val blueprint = blueprint.utilityButton

        if (blueprint.closeGuiButton.enabled) {
            addPerkGuiElement(
                blueprint.closeGuiButton.slot!!,
                CloseGuiButtonGuiElement(blueprint.closeGuiButton.representation!!.toItemStack())
            )
        }

        if (blueprint.disableAllPerksButton.enabled) {
            addPerkGuiElement(
                blueprint.disableAllPerksButton.slot!!,
                DisableAllPerksButtonGuiElement(
                    perkStateController,
                    blueprint.disableAllPerksButton.representation!!.toItemStack()
                )
            )
        }
    }

    override fun getInventory(): Inventory = perkInventory

}