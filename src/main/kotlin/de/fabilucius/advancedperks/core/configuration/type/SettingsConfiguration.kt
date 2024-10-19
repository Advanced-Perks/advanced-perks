package de.fabilucius.advancedperks.core.configuration.type

import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named
import de.fabilucius.advancedperks.core.configuration.AbstractConfiguration
import de.fabilucius.advancedperks.core.configuration.annotation.FilePathInJar
import de.fabilucius.advancedperks.core.logging.APLogger
import java.io.File

@Singleton
@FilePathInJar("settings.yml")
class SettingsConfiguration @Inject constructor(@Named("configurationDirectory") configDir: File, logger: APLogger) :
    AbstractConfiguration(configDir, logger) {

    init {
        APLogger.debugMode = isPluginInDebugMode()
    }

    fun getGlobalPerkLimit(): Int = getInt("global.max_active_perks")

    fun areGuiClickSoundsEnabled(): Boolean = getBoolean("gui.click_sounds")

    fun shouldMetricsBeCollected(): Boolean = getBoolean("collecting_metrics")

    fun isPluginInDebugMode(): Boolean = getBoolean("debug_mode")

}