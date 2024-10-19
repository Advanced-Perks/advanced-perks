package de.fabilucius.advancedperks.core.module

import com.google.inject.AbstractModule
import de.fabilucius.advancedperks.core.configuration.type.*

class ConfigurationModule : AbstractModule() {

    override fun configure() {
        bind(DatabaseConfiguration::class.java).asEagerSingleton()
        bind(MessageConfiguration::class.java).asEagerSingleton()
        bind(PerkGuiConfiguration::class.java).asEagerSingleton()
        bind(PerksConfiguration::class.java).asEagerSingleton()
        bind(SettingsConfiguration::class.java).asEagerSingleton()
    }

}