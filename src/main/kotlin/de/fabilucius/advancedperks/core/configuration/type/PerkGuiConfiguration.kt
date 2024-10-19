package de.fabilucius.advancedperks.core.configuration.type

import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named
import de.fabilucius.advancedperks.core.configuration.AbstractConfiguration
import de.fabilucius.advancedperks.core.configuration.annotation.FilePathInJar
import de.fabilucius.advancedperks.core.logging.APLogger
import de.fabilucius.advancedperks.guisystem.configuration.PerkIconLocation
import java.io.File
import java.util.stream.IntStream

@Singleton
@FilePathInJar("perk_gui.yml")
class PerkGuiConfiguration @Inject constructor(@Named("configurationDirectory") configDir: File, logger: APLogger) :
    AbstractConfiguration(configDir, logger) {

    fun getPerkIconLocations(): List<PerkIconLocation> {
        val locations = getStringList("perk_icon_locations")
            .map { it.split(":") }
            .map { PerkIconLocation(it[0].toInt(), it[1].toInt()) }
            .toList()
        return if (locations.size != 8) getDefaultPerkIconLocations() else locations
    }

    private fun getDefaultPerkIconLocations(): List<PerkIconLocation> {
        val iconLocations = listOf(1, 3, 5, 7, 19, 21, 23, 25)
        val toggleLocations = listOf(10, 12, 14, 16, 28, 30, 32, 34)
        return IntStream.range(0, iconLocations.size)
            .mapToObj { value -> PerkIconLocation(iconLocations[value], toggleLocations[value]) }
            .toList()
    }

    fun getCloseGuiSlot(): Int = getInt("close_gui")

    fun getDisableAllPerksSlot(): Int = getInt("disable_all_perks")

    fun getSetupGuiSlot(): Int = getInt("setup_gui")

    fun getPreviousPageSlot(): Int = getInt("previous_page")

    fun getNextPageSlot(): Int = getInt("next_page")

    fun hasBackground(): Boolean = getBoolean("background")

}