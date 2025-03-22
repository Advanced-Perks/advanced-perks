package de.fabilucius.advancedperks.core.configuration.type

import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named
import de.fabilucius.advancedperks.core.configuration.AbstractConfiguration
import de.fabilucius.advancedperks.core.configuration.annotation.FilePathInJar
import de.fabilucius.advancedperks.core.logging.APLogger
import java.io.File
import java.util.Optional

@Singleton
@FilePathInJar("custom_gui.yml")
class CustomGuiConfiguration @Inject constructor(@Named("configurationDirectory") configDir: File, logger: APLogger) :
    AbstractConfiguration(configDir, logger) {

    fun getActiveCustomGui(): Optional<String> = Optional.ofNullable(getString("active_custom_gui"))

    fun setCustomGui(customGui: String) {
        set("active_custom_gui", customGui)
        saveConfiguration()
    }

}