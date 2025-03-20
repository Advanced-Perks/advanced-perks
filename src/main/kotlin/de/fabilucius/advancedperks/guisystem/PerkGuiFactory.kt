package de.fabilucius.advancedperks.guisystem

import de.fabilucius.advancedperks.guisystem.blueprint.GuiBlueprint
import org.bukkit.entity.Player

interface PerkGuiFactory {
    fun create(blueprint: GuiBlueprint, player: Player): PerkGui
}