package de.fabilucius.advancedperks.guisystem

import com.google.gson.Gson
import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named
import de.fabilucius.advancedperks.AdvancedPerks
import de.fabilucius.advancedperks.core.configuration.type.CustomGuiConfiguration
import de.fabilucius.advancedperks.core.functionextensions.decodeFromBase64
import de.fabilucius.advancedperks.core.functionextensions.toMD5Hash
import de.fabilucius.advancedperks.core.logging.APLogger
import de.fabilucius.advancedperks.guisystem.blueprint.GuiBlueprint
import java.io.File
import java.util.logging.Level

const val CUSTOM_GUI_DIRECTORY = "custom_guis"

@Singleton
class GuiSelectionService @Inject constructor(
    @Named("configurationDirectory") private val pluginDir: File,
    private val configuration: CustomGuiConfiguration,
    private val gson: Gson,
    private val logger: APLogger,
) {

    var activeGuiBlueprint: GuiBlueprint

    init {
        setupCustomGuisDirectory()
        activeGuiBlueprint = loadActiveBlueprintFromConfiguration()
    }

    fun setCustomGuiFromBase64(base64String: String): GuiSetupResult {
        return runCatching {
            val decodedString = base64String.decodeFromBase64()
            activeGuiBlueprint = gson.fromJson(decodedString, GuiBlueprint::class.java)
            val fileName = "${base64String.toMD5Hash()}.json"
            File(pluginDir, "$CUSTOM_GUI_DIRECTORY/$fileName").writeText(decodedString)
            configuration.setCustomGui(fileName)
            GuiSetupResult.BASE64_SUCCESS
        }.getOrElse {
            logger.log(Level.SEVERE, "Unable to parse gui blueprint from base64", it)
            GuiSetupResult.INVALID_BASE64_STRING
        }
    }

    fun setCustomGuiFromFile(fileName: String): GuiSetupResult {
        val guiFile = File(pluginDir, "$CUSTOM_GUI_DIRECTORY${File.separator}$fileName")
        return if (guiFile.exists()) {
            if (guiFile.length() == 0L) {
                logger.warning("Unable to parse gui blueprint from $fileName because the file is empty.")
                GuiSetupResult.INVALID_FILE
            } else {
                runCatching {
                    guiFile.bufferedReader().use { reader ->
                        activeGuiBlueprint = gson.fromJson(reader, GuiBlueprint::class.java)
                    }
                    configuration.setCustomGui(fileName)
                    GuiSetupResult.FILE_SUCCESS
                }.getOrElse {
                    logger.log(Level.SEVERE, "Unable to parse gui blueprint from file ${guiFile.absolutePath}", it)
                    GuiSetupResult.INVALID_FILE
                }
            }
        } else {
            GuiSetupResult.FILE_NOT_FOUND
        }
    }

    private fun loadActiveBlueprintFromConfiguration(): GuiBlueprint {
        val activeCustomGui = configuration.getActiveCustomGui()
        if (activeCustomGui.isPresent) {
            val guiFile = File(pluginDir, "$CUSTOM_GUI_DIRECTORY${File.separator}${activeCustomGui.get()}")
            if (guiFile.exists()) {
                return runCatching { gson.fromJson(guiFile.reader(), GuiBlueprint::class.java) }
                    .getOrElse {
                        logger.log(Level.SEVERE, "Unable to parse gui blueprint from file ${guiFile.absolutePath}", it)
                        loadDefaultBlueprint()
                    }
            } else {
                logger.severe("The active configured custom gui file ${activeCustomGui.get()} does not exist in the custom guis folder. The plugin will now load the default gui blueprint. Either add the missing file back or reconfigure a new custom gui.")
            }
        }
        return loadDefaultBlueprint()
    }

    private fun loadDefaultBlueprint(): GuiBlueprint {
        val defaultBlueprint = this::class.java.classLoader.getResource("default_gui.json")
            ?: throw IllegalStateException("No default gui blueprint found in the plugins jar (is the jar file corrupt?).")
        return gson.fromJson(defaultBlueprint.openStream().reader(), GuiBlueprint::class.java)
    }

    private fun setupCustomGuisDirectory() {
        File(pluginDir, CUSTOM_GUI_DIRECTORY).mkdirs().takeIf { it }?.let {
            logger.info("Successfully created the custom guis directory.")
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