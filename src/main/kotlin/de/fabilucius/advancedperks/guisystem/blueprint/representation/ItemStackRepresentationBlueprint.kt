package de.fabilucius.advancedperks.guisystem.blueprint.representation

import de.fabilucius.advancedperks.core.functionextensions.translateColorCodes
import de.fabilucius.advancedperks.core.itembuilder.types.ItemStackBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

data class ItemStackRepresentationBlueprint(
    val title: String,
    val material: Material,
    val amount: Int,
    val glowing: Boolean,
) : GuiRepresentationBlueprint {

    override fun toItemStack(placeholder: Map<String, String>): ItemStack = ItemStackBuilder.fromMaterial(material)
        .setDisplayName(replacePlaceholders(title.translateColorCodes(), placeholder))
        .setAmount(amount)
        //TODO add glowing
        .build()

}
