package de.fabilucius.advancedperks.core.functionextensions

import org.bukkit.ChatColor
import java.security.MessageDigest
import java.util.Base64

fun String.translateColorCodes(): String = ChatColor.translateAlternateColorCodes('&', this)

fun String.decodeFromBase64(): String = String(Base64.getDecoder().decode(this))

fun String.toMD5Hash(): String = MessageDigest.getInstance("MD5").digest(this.toByteArray()).joinToString("") { "%02x".format(it) }