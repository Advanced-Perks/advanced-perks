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

@Singleton
class GuiSystemManager @Inject constructor(
    private val httpClient: HttpClient,
    private val gson: Gson,
    private val perkGuiFactory: PerkGuiFactory,
) {

    val managedGuis: MutableMap<UUID, PerkGui> = mutableMapOf()

    //TODO implement multiple ways to retrieve gui data
    fun openGui(player: Player) {
        val guiBlueprint = fetchGuiBlueprint()
        val perkGui = perkGuiFactory.create(guiBlueprint, player)
        managedGuis[perkGui.player.uniqueId] = perkGui
        player.openInventory(perkGui.perkInventory)
    }

    private fun fetchGuiBlueprint(): GuiBlueprint {
        // fetch via url with gson
        val url ="https://raw.githubusercontent.com/Advanced-Perks/infrastructure/refs/heads/main/gui_blueprint.json"
        val response = httpClient.send(
            HttpRequest.newBuilder().uri(URI.create(url)).build(),
            HttpResponse.BodyHandlers.ofString()
        )
        return gson.fromJson(response.body(), GuiBlueprint::class.java)
    }

}