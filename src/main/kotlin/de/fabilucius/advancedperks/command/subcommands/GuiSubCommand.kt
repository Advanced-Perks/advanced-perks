package de.fabilucius.advancedperks.command.subcommands

import com.google.inject.Inject
import de.fabilucius.advancedperks.core.command.AbstractSubCommand
import de.fabilucius.advancedperks.core.command.annotation.CommandIdentifier
import de.fabilucius.advancedperks.guisystem.GuiSystemManager
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

const val URL = "https://raw.githubusercontent.com/Advanced-Perks/infrastructure/refs/heads/main/gui_blueprint.json?token=\$(date%20+%s)"

@CommandIdentifier("gui")
class GuiSubCommand @Inject constructor(
    private val guiManager: GuiSystemManager
) : AbstractSubCommand() {

    override fun executeCommand(commandSender: CommandSender?, vararg arguments: String?) {
        guiManager.openGui(commandSender as Player)
    }

    override fun tabComplete(commandSender: CommandSender?, vararg arguments: String?): MutableList<String>  = mutableListOf()
}