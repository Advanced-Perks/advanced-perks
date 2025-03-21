package de.fabilucius.advancedperks.command.subcommands

import com.google.inject.Inject
import de.fabilucius.advancedperks.AdvancedPerks
import de.fabilucius.advancedperks.core.command.AbstractSubCommand
import de.fabilucius.advancedperks.core.command.annotation.CommandIdentifier
import de.fabilucius.advancedperks.core.configuration.replace.ReplaceOptions
import de.fabilucius.advancedperks.core.configuration.type.MessageConfiguration
import de.fabilucius.advancedperks.guisystem.CUSTOM_GUI_DIRECTORY
import de.fabilucius.advancedperks.guisystem.GuiSelectionService
import de.fabilucius.advancedperks.guisystem.GuiSetupResult
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.File

const val CONFIGURATION_PREFIX = "command.perks.setgui"

@CommandIdentifier("setgui")
class SetGuiSubCommand @Inject constructor(
    private val guiSelectionService: GuiSelectionService,
    private val plugin: AdvancedPerks,
    private val configuration: MessageConfiguration,
) : AbstractSubCommand() {

    override fun executeCommand(commandSender: CommandSender, vararg arguments: String?) {
        if (arguments.size == 2) {
            val result = if (arguments[0].equals("file", true)) {
                guiSelectionService.setCustomGuiFromFile(arguments[1]!!)
            } else if (arguments[0].equals("base64", true)) {
                guiSelectionService.setCustomGuiFromBase64(arguments[1]!!)
            } else {
                GuiSetupResult.WRONG_COMMAND_USAGE
            }
            when (result) {
                GuiSetupResult.FILE_SUCCESS -> commandSender.sendMessage(
                    configuration.getMessage(
                        "$CONFIGURATION_PREFIX.file.success",
                        ReplaceOptions("<file>", arguments[1]!!)
                    )
                )

                GuiSetupResult.BASE64_SUCCESS -> commandSender.sendMessage(configuration.getMessage("$CONFIGURATION_PREFIX.base64.success"))
                GuiSetupResult.INVALID_FILE -> commandSender.sendMessage(
                    configuration.getMessage(
                        "$CONFIGURATION_PREFIX.file.invalid_file",
                        ReplaceOptions("<file>", arguments[1]!!)
                    )
                )

                GuiSetupResult.INVALID_BASE64_STRING -> commandSender.sendMessage(configuration.getMessage("$CONFIGURATION_PREFIX.base64.invalid"))
                GuiSetupResult.FILE_NOT_FOUND -> commandSender.sendMessage(
                    configuration.getMessage(
                        "$CONFIGURATION_PREFIX.file.file_not_found",
                        ReplaceOptions("<file>", arguments[1]!!)
                    )
                )

                GuiSetupResult.WRONG_COMMAND_USAGE -> commandSender.sendMessage(configuration.getMessage("$CONFIGURATION_PREFIX.syntax"))
            }
        } else {
            commandSender.sendMessage(configuration.getMessage("$CONFIGURATION_PREFIX.syntax"))
        }
    }

    override fun tabComplete(commandSender: CommandSender?, vararg arguments: String?): MutableList<String> {
        return when (arguments.size) {
            1 -> mutableListOf("file", "base64")
            2 -> if (arguments[0] == "file") {
                File(plugin.dataFolder, CUSTOM_GUI_DIRECTORY).listFiles()?.map { it.name }?.toMutableList()
                    ?: mutableListOf()
            } else {
                mutableListOf()
            }

            else -> mutableListOf()
        }
    }

}