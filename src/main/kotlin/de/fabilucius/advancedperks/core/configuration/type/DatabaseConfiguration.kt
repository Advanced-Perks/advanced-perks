package de.fabilucius.advancedperks.core.configuration.type

import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named
import de.fabilucius.advancedperks.core.configuration.AbstractConfiguration
import de.fabilucius.advancedperks.core.configuration.annotation.FilePathInJar
import de.fabilucius.advancedperks.core.database.Credentials
import de.fabilucius.advancedperks.core.database.SaveType
import de.fabilucius.advancedperks.core.logging.APLogger
import java.io.File

@Singleton
@FilePathInJar("database.yml")
class DatabaseConfiguration @Inject constructor(@Named("configurationDirectory") configDir: File, logger: APLogger) :
    AbstractConfiguration(configDir, logger) {

    fun getFileLocation(): String = this.getString("file.file_location")!!

    fun getSqlUrl(): String = this.getString("database.sql_url")!!

    fun getCredentials(): Credentials = Credentials.withAuth(
        this.getString("database.credentials.username")!!,
        this.getString("database.credentials.password")!!
    )

    fun getSaveType(): SaveType = SaveType.entries.find {
        it.name.equals(getString("save_type")!!, true)
    } ?: SaveType.UNKNOWN

}