package de.fabilucius.advancedperks.command.subcommands

import com.google.inject.Inject
import com.google.inject.name.Named
import de.fabilucius.advancedperks.core.command.AbstractSubCommand
import de.fabilucius.advancedperks.core.command.annotation.CommandIdentifier
import de.fabilucius.advancedperks.core.command.annotation.Permission
import de.fabilucius.advancedperks.core.configuration.replace.ReplaceOptions
import de.fabilucius.advancedperks.core.configuration.type.MessageConfiguration
import de.fabilucius.advancedperks.core.functionextensions.sendMessage
import de.fabilucius.advancedperks.guisystem.CUSTOM_GUI_DIRECTORY
import de.fabilucius.advancedperks.guisystem.GuiSelectionService
import de.fabilucius.advancedperks.guisystem.GuiSetupResult
import org.bukkit.command.CommandSender
import java.io.File

const val CONFIGURATION_PREFIX = "command.perks.setgui"

@CommandIdentifier("setgui")
@Permission("advancedperks.command.setgui")
class SetGuiSubCommand @Inject constructor(
    private val guiSelectionService: GuiSelectionService,
    @Named("configurationDirectory") private val pluginDirection: File,
    private val configuration: MessageConfiguration,
) : AbstractSubCommand() {

    override fun executeCommand(commandSender: CommandSender, vararg arguments: String) {
        if (arguments.size == 2) {
            val result = when {
                arguments[0].equals("file", true) -> guiSelectionService.setCustomGuiFromFile(arguments[1])
                arguments[0].equals("base64", true) -> guiSelectionService.setCustomGuiFromBase64(arguments[1])
                else -> GuiSetupResult.WRONG_COMMAND_USAGE
            }

            val fileReplaceOption = ReplaceOptions("<file>", arguments[1])

            when (result) {
                GuiSetupResult.FILE_SUCCESS -> configuration.getMessage(
                    "$CONFIGURATION_PREFIX.file.success", fileReplaceOption
                ).sendMessage(commandSender)

                GuiSetupResult.BASE64_SUCCESS -> configuration.getMessage("$CONFIGURATION_PREFIX.base64.success")
                    .sendMessage(commandSender)

                GuiSetupResult.INVALID_FILE -> configuration.getMessage(
                    "$CONFIGURATION_PREFIX.file.invalid_file", fileReplaceOption
                ).sendMessage(commandSender)

                GuiSetupResult.INVALID_BASE64_STRING -> configuration.getMessage("$CONFIGURATION_PREFIX.base64.invalid")
                    .sendMessage(commandSender)

                GuiSetupResult.FILE_NOT_FOUND -> configuration.getMessage(
                    "$CONFIGURATION_PREFIX.file.file_not_found", fileReplaceOption
                ).sendMessage(commandSender)

                GuiSetupResult.WRONG_COMMAND_USAGE -> configuration.getMessage("$CONFIGURATION_PREFIX.syntax")
                    .sendMessage(commandSender)
            }
        } else {
            commandSender.sendMessage(configuration.getMessage("$CONFIGURATION_PREFIX.syntax"))
        }
    }

    override fun tabComplete(commandSender: CommandSender?, vararg arguments: String): MutableList<String> =
        when (arguments.size) {
            1 -> listOf("file", "base64").filter { it.startsWith(arguments[0], true) }.toMutableList()
            2 -> when (arguments[0].lowercase()) {
                "file" -> calculateEligibleGuiFiles(arguments)
                "base64" -> mutableListOf("<insert base64 string of gui>")
                else -> mutableListOf()
            }

            else -> mutableListOf()
        }

    private fun calculateEligibleGuiFiles(arguments: Array<out String>): MutableList<String> =
        File(pluginDirection, CUSTOM_GUI_DIRECTORY).listFiles()?.filter { it.name.endsWith(".json", true) }
            ?.map { it.name }?.filter { it.startsWith(arguments[1], true) }?.toMutableList()
            ?.apply { if (isEmpty()) add("<no eligible json files found>") }
            ?: mutableListOf("<no /$CUSTOM_GUI_DIRECTORY directory found in the plugin's directory>")

}