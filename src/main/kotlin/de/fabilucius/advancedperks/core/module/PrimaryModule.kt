package de.fabilucius.advancedperks.core.module

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.inject.AbstractModule
import com.google.inject.assistedinject.FactoryModuleBuilder
import com.google.inject.name.Names
import com.google.inject.util.Providers
import de.fabilucius.advancedperks.AdvancedPerks
import de.fabilucius.advancedperks.api.AdvancedPerksApi
import de.fabilucius.advancedperks.api.AdvancedPerksApiImpl
import de.fabilucius.advancedperks.api.placeholderapi.AdvancedPerksEnabledExpansion
import de.fabilucius.advancedperks.api.placeholderapi.AdvancedPerksPerkLimitExpansion
import de.fabilucius.advancedperks.api.placeholderapi.AdvancedPerksUseExpansion
import de.fabilucius.advancedperks.compatabilities.CompatibilityController
import de.fabilucius.advancedperks.core.database.Database
import de.fabilucius.advancedperks.core.database.DatabaseProvider
import de.fabilucius.advancedperks.core.economy.EconomyController
import de.fabilucius.advancedperks.core.economy.interfaces.EconomyInterface
import de.fabilucius.advancedperks.core.economy.interfaces.types.VaultEconomyInterface
import de.fabilucius.advancedperks.core.logging.APLogger
import de.fabilucius.advancedperks.data.PerkDataRepository
import de.fabilucius.advancedperks.data.state.PerkStateController
import de.fabilucius.advancedperks.guisystem.GuiSystemEventHandler
import de.fabilucius.advancedperks.guisystem.GuiSystemManager
import de.fabilucius.advancedperks.guisystem.PerkGuiFactory
import de.fabilucius.advancedperks.guisystem.blueprint.representation.GuiElementRepresentationTypeAdapter
import de.fabilucius.advancedperks.guisystem.blueprint.representation.GuiRepresentationBlueprint
import de.fabilucius.advancedperks.registry.PerkRegistry
import de.fabilucius.advancedperks.registry.PerkRegistryImpl
import de.fabilucius.advancedperks.registry.loader.PerkYmlLoader
import de.fabilucius.advancedperks.updatechecker.UpdateChecker
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import java.io.File
import java.net.http.HttpClient

class PrimaryModule(private val advancedPerks: AdvancedPerks) : AbstractModule() {

    override fun configure() {
        bind(AdvancedPerks::class.java).toInstance(this.advancedPerks)
        bind(File::class.java).annotatedWith(Names.named("configurationDirectory")).toInstance(
            advancedPerks.dataFolder
        )
        bind(APLogger::class.java).asEagerSingleton()
        bind(PerkRegistry::class.java).to(PerkRegistryImpl::class.java)
        bind(PerkYmlLoader::class.java).asEagerSingleton()
        bind(PerkDataRepository::class.java).asEagerSingleton()
        bind(Database::class.java).toProvider(
            DatabaseProvider::class.java
        )
        bind(PerkStateController::class.java).asEagerSingleton()
        bind(AdvancedPerksApi::class.java).to(AdvancedPerksApiImpl::class.java)
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            bind(AdvancedPerksUseExpansion::class.java).asEagerSingleton()
            bind(AdvancedPerksEnabledExpansion::class.java).asEagerSingleton()
            bind(AdvancedPerksPerkLimitExpansion::class.java).asEagerSingleton()
        }
        bind(EconomyController::class.java).asEagerSingleton()
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            bind(EconomyInterface::class.java).to(VaultEconomyInterface::class.java)
        } else {
            bind(EconomyInterface::class.java).toProvider(Providers.of<EconomyInterface?>(null))
        }
        bind(CompatibilityController::class.java).asEagerSingleton()
        bind(UpdateChecker::class.java).asEagerSingleton()
        //install(FactoryModuleBuilder().build(PerkGuiFactory::class.java))
        install(FactoryModuleBuilder().build(PerkGuiFactory::class.java))
        bind(Gson::class.java).toInstance(GsonBuilder()
            .registerTypeAdapter(GuiRepresentationBlueprint::class.java, GuiElementRepresentationTypeAdapter())
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create())
        bind(HttpClient::class.java).toInstance(HttpClient.newBuilder().build())
        bind(GuiSystemManager::class.java).asEagerSingleton()
        bind(GuiSystemEventHandler::class.java).asEagerSingleton()
    }

}
