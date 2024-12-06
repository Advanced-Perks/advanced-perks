package de.fabilucius.advancedperks.command.subcommands

import com.google.gson.GsonBuilder
import com.google.inject.Inject
import com.google.inject.Injector
import de.fabilucius.advancedperks.core.command.AbstractSubCommand
import de.fabilucius.advancedperks.core.command.annotation.CommandIdentifier
import de.fabilucius.advancedperks.core.gui.blueprint.PerkGuiBlueprint
import de.fabilucius.advancedperks.core.gui.blueprint.representation.GuiElementRepresentationBlueprint
import de.fabilucius.advancedperks.core.gui.blueprint.representation.GuiElementRepresentationTypeAdapter
import de.fabilucius.advancedperks.core.gui.interpreter.GuiInterpreter
import org.apache.commons.io.IOUtils
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.InputStreamReader
import java.net.URL

const val URL = "https://raw.githubusercontent.com/Advanced-Perks/infrastructure/refs/heads/main/gui_blueprint.json?token=\$(date%20+%s)"

@CommandIdentifier("gui")
class GuiSubCommand @Inject constructor(
    private val injector: Injector
) : AbstractSubCommand() {

    override fun executeCommand(commandSender: CommandSender?, vararg arguments: String?) {
        val blueprint = fetchJsonFromUrl()

        commandSender!!.sendMessage(blueprint.toString())

        val inventory = injector.getInstance(GuiInterpreter::class.java).interpretBlueprint(blueprint!!)
        (commandSender as Player).openInventory(inventory)
    }

    override fun tabComplete(commandSender: CommandSender?, vararg arguments: String?): MutableList<String> =
        mutableListOf()

    private fun fetchJsonFromUrl(): PerkGuiBlueprint? {
        val connection = URL(URL).openConnection()
        connection.connect()
        val inputStream = connection.getInputStream()
        val reader = InputStreamReader(inputStream)
        val content = IOUtils.toString(reader)
        println(content)
        return GsonBuilder().registerTypeAdapter(GuiElementRepresentationBlueprint::class.java, GuiElementRepresentationTypeAdapter()).create().fromJson(content, PerkGuiBlueprint::class.java)
    }
}