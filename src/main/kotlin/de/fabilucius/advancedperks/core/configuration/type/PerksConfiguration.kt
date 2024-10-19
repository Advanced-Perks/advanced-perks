package de.fabilucius.advancedperks.core.configuration.type

import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named
import de.fabilucius.advancedperks.core.configuration.AbstractMessageConfiguration
import de.fabilucius.advancedperks.core.configuration.annotation.FilePathInJar
import de.fabilucius.advancedperks.core.logging.APLogger
import de.fabilucius.advancedperks.perk.properties.PerkDescription
import de.fabilucius.advancedperks.perk.properties.PerkGuiIcon
import java.io.File

@Singleton
@FilePathInJar("perks.yml")
class PerksConfiguration @Inject constructor(
    @Named("configurationDirectory") configDir: File,
    logger: APLogger,
    private val messageConfiguration: MessageConfiguration
) : AbstractMessageConfiguration(configDir, logger) {

    fun getDisplayName(identifier: String): String = getMessage("$identifier.display_name")

    fun getPerkDescription(identifier: String): PerkDescription =
        PerkDescription(getMessageList("$identifier.description"))

    fun getPerkGuiIcon(identifier: String): PerkGuiIcon = PerkGuiIcon(getString("$identifier.icon"))

    fun isEnabled(identifier: String): Boolean = getBoolean("$identifier.enabled")

    //TODO Test this to see if it works
    fun getFlags(identifier: String): Map<String, Any> {
        val configSection = getConfigurationSection("$identifier.flags")
        return configSection?.getKeys(false)
            ?.filter { get("$identifier.flags.$it") != null }
            ?.associate { it to get("$identifier.flags.$it")!! } ?: emptyMap()
    }

    fun setPrice(identifier: String, price: Double): Map<String, Any> {
        return getFlags(identifier).toMutableMap().apply {
            this["price"] = price
            set("$identifier.flags", this)
        }
    }

    override fun getPrefix() = messageConfiguration.getPrefix()
}