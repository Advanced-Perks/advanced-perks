package de.fabilucius.advancedperks.core.configuration.type

import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named
import de.fabilucius.advancedperks.core.configuration.AbstractConfiguration
import de.fabilucius.advancedperks.core.configuration.annotation.FilePathInJar
import de.fabilucius.advancedperks.core.logging.APLogger
import java.io.File

@Singleton
@FilePathInJar("perk_gui.yml")
class PerkGuiConfiguration @Inject constructor(@Named("configurationDirectory") configDir: File, logger: APLogger) :
    AbstractConfiguration(configDir, logger) {

}