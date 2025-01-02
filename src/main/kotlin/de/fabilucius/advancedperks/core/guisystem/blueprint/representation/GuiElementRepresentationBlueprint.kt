package de.fabilucius.advancedperks.core.guisystem.blueprint.representation

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

interface GuiElementRepresentationBlueprint {

    fun toItemStack(): ItemStack

}

class GuiElementRepresentationTypeAdapter : TypeAdapter<GuiElementRepresentationBlueprint>() {

    override fun write(p0: JsonWriter?, p1: GuiElementRepresentationBlueprint?) {
        TODO("Not yet needed")
    }

    override fun read(reader: JsonReader): GuiElementRepresentationBlueprint {
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

