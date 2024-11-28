package de.fabilucius.advancedperks.core.gui.blueprint.representation

import com.google.gson.*
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.bukkit.Material

class GuiElementRepresentationTypeAdapter : TypeAdapter<GuiElementRepresentationBlueprint>() {
    override fun write(out: JsonWriter, value: GuiElementRepresentationBlueprint?) {
        // Writing logic if needed
        TODO("Not implemented for writing")
    }

    override fun read(reader: JsonReader): GuiElementRepresentationBlueprint {
        var title: String? = null
        var material: Material? = null
        var amount: Int? = null
        var glowing: Boolean? = null
        var textureValue: String? = null

        reader.beginObject()
        //TODO test if its rigid enough to be handle every possible order of the fields in the json
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
            material != null && amount != null && glowing != null ->
                ItemStackGuiElementRepresentationBlueprint(
                    title = title ?: "Unnamed ItemStack",
                    material = material,
                    amount = amount,
                    glowing = glowing
                )
            textureValue != null ->
                TexturedSkullGuiElementRepresentationBlueprint(
                    title = title ?: "Unnamed Skull",
                    textureValue = textureValue
                )
            else -> throw IllegalArgumentException("Unable to determine representation type")
        }
    }
}

