package de.fabilucius.advancedperks.core.functionextensions

import org.bukkit.ChatColor

fun String.translateColorCodes(): String = ChatColor.translateAlternateColorCodes('&', this)