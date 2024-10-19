package de.fabilucius.advancedperks.core.logging

import com.google.inject.Inject
import com.google.inject.Singleton
import de.fabilucius.advancedperks.AdvancedPerks
import java.util.logging.Level
import java.util.logging.Logger

const val DEBUG_LOG_PREFIX = "[debug]"

@Singleton
class APLogger @Inject constructor(
    plugin: AdvancedPerks,
) : Logger("ap", null) {

    init {
        parent = plugin.server.logger
    }

    @JvmOverloads
    fun debug(message: Any, throwable: Throwable? = null) {
        if (debugMode) {
            log(Level.INFO, "$DEBUG_LOG_PREFIX $message", throwable)
        }
    }

    companion object {

        var debugMode: Boolean = false

    }
}