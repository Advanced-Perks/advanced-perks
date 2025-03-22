package de.fabilucius.advancedperks.guisystem.blueprint.representation

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import de.fabilucius.advancedperks.core.functionextensions.translateColorCodes
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

//TODO implement
interface GuiRepresentationBlueprint {

    fun toItemStack(placeholder: Map<String, String> = emptyMap()): ItemStack

    fun replacePlaceholders(title: String, placeholder: Map<String, String>): String {
        return placeholder.entries.fold(title) { acc, (key, value) -> acc.replace("{$key}", value) }.translateColorCodes()
    }

}

class GuiElementRepresentationTypeAdapter : TypeAdapter<GuiRepresentationBlueprint>() {

    override fun write(p0: JsonWriter?, p1: GuiRepresentationBlueprint?) {
        TODO("Not yet needed")
    }

    override fun read(reader: JsonReader): GuiRepresentationBlueprint {
        var title: String? = null
        var material: Material? = null
        var amount: Int? = null
        var glowing: Boolean? = null
        var textureValue: String? = null

        reader.beginObject()

        while (reader.hasNext()) {
            when (reader.nextName()) {
                "title" -> title = reader.nextString()
                "material" -> material = Material.valueOf(reader.nextString())
                "amount" -> amount = reader.nextInt()
                "glowing" -> glowing = reader.nextBoolean()
                "textureValue" -> textureValue = reader.nextString()
                else -> reader.skipValue()
            }
        }

        reader.endObject()

        return when {
            title != null && material != null && amount != null && glowing != null ->
                ItemStackRepresentationBlueprint(
                    title = title,
                    material = material,
                    amount = amount,
                    glowing = glowing
                )

            title != null && textureValue != null ->
                TexturedHeadRepresentationBlueprint(
                    title = title,
                    textureValue = textureValue
                )

            else -> throw IllegalArgumentException("Unable to determine representation type")
        }
    }

}