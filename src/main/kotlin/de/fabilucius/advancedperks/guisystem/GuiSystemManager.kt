package de.fabilucius.advancedperks.guisystem

import com.google.gson.Gson
import com.google.inject.Inject
import com.google.inject.Singleton
import de.fabilucius.advancedperks.guisystem.blueprint.GuiBlueprint
import org.bukkit.entity.Player
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.UUID

const val DEFAULT_GUI_URL = "https://raw.githubusercontent.com/Advanced-Perks/infrastructure/refs/heads/main/gui_blueprint.json?token=\$(date%20+%s)"

@Singleton
class GuiSystemManager @Inject constructor(
    private val httpClient: HttpClient,
    private val gson: Gson,
    private val perkGuiFactory: PerkGuiFactory,
    val managedGuis: MutableMap<UUID, PerkGui> = mutableMapOf(),
) {

    //TODO implement multiple ways to retrieve gui data
    fun openGui(player: Player) {
        val guiBlueprint = fetchGuiBlueprint()
        val perkGui = perkGuiFactory.create(guiBlueprint, player)
        managedGuis[perkGui.id] = perkGui
        player.openInventory(perkGui.inventory)
    }

    private fun fetchGuiBlueprint(): GuiBlueprint {
        // fetch via url with gson
        val response = httpClient.send(HttpRequest.newBuilder().uri(URI.create(DEFAULT_GUI_URL)).build(), HttpResponse.BodyHandlers.ofString())
        return gson.fromJson(response.body(), GuiBlueprint::class.java)
    }

}