package de.fabilucius.advancedperks.guisystem

import com.google.gson.Gson
import com.google.inject.Inject
import com.google.inject.Singleton
import de.fabilucius.advancedperks.AdvancedPerks
import de.fabilucius.advancedperks.core.configuration.type.CustomGuiConfiguration
import de.fabilucius.advancedperks.core.functionextensions.decodeFromBase64
import de.fabilucius.advancedperks.core.functionextensions.toMD5Hash
import de.fabilucius.advancedperks.guisystem.blueprint.GuiBlueprint
import java.io.File

const val CUSTOM_GUI_DIRECTORY = "custom_guis"

@Singleton
class GuiSelectionService @Inject constructor(
    private val plugin: AdvancedPerks,
    private val configuration: CustomGuiConfiguration,
    private val gson: Gson,
) {

    var activeGuiBlueprint: GuiBlueprint

    init {
        setupCustomGuisDirectory()
        activeGuiBlueprint = setupActiveGuiBlueprint()
    }

    fun setCustomGuiFromBase64(base64String: String): GuiSetupResult {
        return try {
            val decodedString = base64String.decodeFromBase64()
            activeGuiBlueprint = gson.fromJson(decodedString, GuiBlueprint::class.java)
            val fileName = "${base64String.toMD5Hash()}.json"
            File(plugin.dataFolder, "$CUSTOM_GUI_DIRECTORY/$fileName").writeText(decodedString)
            configuration.setCustomGui(fileName)
            GuiSetupResult.BASE64_SUCCESS
        } catch (ex: Exception) {
            GuiSetupResult.INVALID_BASE64_STRING
        }
    }

    fun setCustomGuiFromFile(fileName: String): GuiSetupResult {
        val guiFile = File(plugin.dataFolder, CUSTOM_GUI_DIRECTORY + File.separator + fileName)
        if (guiFile.exists()) {
            try {
                activeGuiBlueprint = gson.fromJson(guiFile.reader(), GuiBlueprint::class.java)
                configuration.setCustomGui(fileName)
                return GuiSetupResult.FILE_SUCCESS
            } catch (ex: Exception) {
                return GuiSetupResult.INVALID_FILE
            }
        } else {
            return GuiSetupResult.FILE_NOT_FOUND
        }
    }

    private fun setupActiveGuiBlueprint(): GuiBlueprint {
        val activeCustomGui = configuration.getActiveCustomGui()
        if (activeCustomGui.isPresent) {
            val file = activeCustomGui.get()
            val guiFile = File(plugin.dataFolder, CUSTOM_GUI_DIRECTORY + File.separator + file)
            if (guiFile.exists()) {
                return gson.fromJson(guiFile.reader(), GuiBlueprint::class.java)
            }
        }

        val defaultBlueprint = this::class.java.classLoader.getResource("default_gui.json")
            ?: throw IllegalStateException("Default GUI blueprint not found")
        return gson.fromJson(defaultBlueprint.openStream().reader(), GuiBlueprint::class.java)
    }

    private fun setupCustomGuisDirectory() {
        File(plugin.dataFolder, CUSTOM_GUI_DIRECTORY).mkdirs().takeIf { it }?.let {
            plugin.logger.info("Successfully created the custom guis directory.")
        }
    }

}

enum class GuiSetupResult {
    FILE_SUCCESS,
    BASE64_SUCCESS,
    FILE_NOT_FOUND,
    INVALID_FILE,
    INVALID_BASE64_STRING,
    WRONG_COMMAND_USAGE,
}