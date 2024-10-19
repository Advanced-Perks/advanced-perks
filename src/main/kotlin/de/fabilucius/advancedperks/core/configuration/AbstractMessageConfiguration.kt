package de.fabilucius.advancedperks.core.configuration

import de.fabilucius.advancedperks.core.configuration.replace.ReplaceOptions
import de.fabilucius.advancedperks.core.logging.APLogger
import org.bukkit.ChatColor
import java.io.File

abstract class AbstractMessageConfiguration(configDir: File, logger: APLogger) : AbstractConfiguration(configDir, logger) {

    fun getMessageList(key: String, vararg replaceOptions: ReplaceOptions): List<String> =
        getStringList(key).takeIf { it.isNotEmpty() }?.let {
            it.map { message -> computeMessage(message, replaceOptions) }
        } ?: listOf("Message list for key $key not found.")

    fun getMessage(key: String, vararg replaceOptions: ReplaceOptions): String =
        getString(key)?.let {
            computeMessage(it, replaceOptions)
        } ?: "Message for key $key not found."

    private fun computeMessage(messageToCompute: String, replaceOptions: Array<out ReplaceOptions>): String =
        messageToCompute.let { message ->
            replaceOptions.fold(message) { acc, option ->
                acc.replace(option.replaceKey, option.replaceData)
            }.replace("<prefix>", getPrefix())
        }.let { ChatColor.translateAlternateColorCodes('&', it) }

    abstract fun getPrefix(): String

}