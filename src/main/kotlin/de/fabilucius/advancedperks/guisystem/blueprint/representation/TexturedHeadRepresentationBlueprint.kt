package de.fabilucius.advancedperks.guisystem.blueprint.representation

import de.fabilucius.advancedperks.core.functionextensions.translateColorCodes
import de.fabilucius.advancedperks.core.itembuilder.types.skull.SkullStackBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

data class TexturedHeadRepresentationBlueprint(
    val title: String,
    val textureValue: String,
) : GuiRepresentationBlueprint {

    override fun toItemStack(placeholder: Map<String, String>): ItemStack = SkullStackBuilder.fromMaterial(Material.PLAYER_HEAD)
        .setBase64Value(textureValue)
        .setDisplayName(replacePlaceholders(title.translateColorCodes(), placeholder))
        .build()

}
