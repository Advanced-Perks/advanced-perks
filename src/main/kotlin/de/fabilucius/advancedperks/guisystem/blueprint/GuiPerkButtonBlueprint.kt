package de.fabilucius.advancedperks.guisystem.blueprint

import de.fabilucius.advancedperks.guisystem.blueprint.representation.GuiRepresentationBlueprint
import de.fabilucius.advancedperks.guisystem.model.ActivationIndicator

data class GuiPerkButtonBlueprint(
    val activationIndicator: ActivationIndicator,
    val inventorySlots: List<Int>,
    val toggleButton: GuiPerkToggleButtonBlueprint,
)

data class GuiPerkToggleButtonBlueprint(
    val enabled: Boolean,
    val inventorySlots: List<Int>?,
    val activeRepresentation: GuiRepresentationBlueprint?,
    val inactiveRepresentation: GuiRepresentationBlueprint?,
)
