package de.fabilucius.advancedperks.core.configuration

import com.google.inject.Inject
import com.google.inject.name.Named
import de.fabilucius.advancedperks.core.configuration.annotation.FilePathInJar
import de.fabilucius.advancedperks.core.logging.APLogger
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.StandardCopyOption

abstract class AbstractConfiguration @Inject constructor(
    @Named("configurationDirectory") private val configDir: File,
    private val logger: APLogger,
) : YamlConfiguration() {

    private val fallbackConfiguration = YamlConfiguration()
    private val configurationPath: String? = this::class.java.getAnnotation(FilePathInJar::class.java)?.value

    init {
        check(!configurationPath.isNullOrEmpty()) {
            "The configuration class ${this::class.qualifiedName} is missing a correct @FilePathInJar annotation."
        }
        extractAndLoadConfiguration()
    }

    fun saveConfiguration() = save(File(configDir, configurationPath!!))

    override fun get(path: String, def: Any?): Any? {
        val result = super.get(path, null)
        if (result == null) {
            logger.warning("The configuration path '$path' is missing in the configuration file '${this::class.simpleName}' trying to resort to the fallback value.")
            val fallbackResult = fallbackConfiguration[path, def]
            return if (fallbackResult == null) {
                logger.warning("No fallback value found for the configuration path '$path' in the fallback configuration file.")
                def;
            } else {
                this[path] = fallbackResult
                this.saveConfiguration()
                fallbackResult
            }
        }
        return super.get(path, def)
    }

    private fun extractAndLoadConfiguration() {
        val file = File(configDir, configurationPath!!)
        val jarResourceUrl = this::class.java.classLoader.getResource(configurationPath)
        check(jarResourceUrl != null) {
            "The configuration file $configurationPath could not be found in the jar."
        }
        load(file)
        jarResourceUrl.openStream().use { stream ->
            if (!file.exists()) {
                file.parentFile.mkdirs()
                Files.copy(stream, file.toPath(), StandardCopyOption.REPLACE_EXISTING)
            }
            fallbackConfiguration.load(InputStreamReader(stream))
        }
    }

}