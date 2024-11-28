package de.fabilucius.advancedperks.core.gui.blueprint.representation

import de.fabilucius.advancedperks.core.itembuilder.types.ItemStackBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

data class ItemStackGuiElementRepresentationBlueprint(
    val title: String,
    val material: Material,
    val amount: Int,
    val glowing: Boolean,
) : GuiElementRepresentationBlueprint {

    override fun toItemStack(): ItemStack = ItemStackBuilder.fromMaterial(material)
        .setAmount(amount)
        .setDisplayName(title)
        .build()
}