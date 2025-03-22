package de.fabilucius.advancedperks.core.functionextensions

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import java.security.MessageDigest
import java.util.Base64

fun String.decodeFromBase64(): String = String(Base64.getDecoder().decode(this))

fun String.toMD5Hash(): String =
    MessageDigest.getInstance("MD5").digest(this.toByteArray()).joinToString("") { "%02x".format(it) }

/* Spigot related extension functions */

fun String.sendMessage(commandSender: CommandSender) = commandSender.sendMessage(this)

fun String.translateColorCodes(): String = ChatColor.translateAlternateColorCodes('&', this)