package de.fabilucius.advancedperks.core.guisystem.blueprint.representation

import de.fabilucius.advancedperks.core.itembuilder.types.skull.SkullStackBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

data class TexturedHeadRepresentationBlueprint(
    val title: String,
    val textureValue: String,
) : GuiElementRepresentationBlueprint {

    override fun toItemStack(): ItemStack = SkullStackBuilder.fromMaterial(Material.PLAYER_HEAD)
        .setBase64Value(textureValue)
        .setDisplayName(title)
        .build()

}
