package de.fabilucius.advancedperks.core.gui.blueprint.representation

import de.fabilucius.advancedperks.core.itembuilder.types.skull.SkullStackBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

data class TexturedSkullGuiElementRepresentationBlueprint(
    val title: String,
    val textureValue: String,
) : GuiElementRepresentationBlueprint {

    override fun toItemStack(): ItemStack = SkullStackBuilder.fromMaterial(Material.PLAYER_HEAD)
        .setBase64Value(textureValue)
        .setDisplayName(title)
        .build()
}