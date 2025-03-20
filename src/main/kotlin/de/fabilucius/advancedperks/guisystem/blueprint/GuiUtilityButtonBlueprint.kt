package de.fabilucius.advancedperks.guisystem.blueprint

data class GuiUtilityButtonBlueprint(
    val closeGuiButton: GuiCloseGuiButtonBlueprint,
    val disableAllPerksButton: GuiDisableAllPerksButtonBlueprint,
)

data class GuiCloseGuiButtonBlueprint(
    val enabled: Boolean,
    val slot: Int?,
    val representation: GuiRepresentationBlueprint?,
)

data class GuiDisableAllPerksButtonBlueprint(
    val enabled: Boolean,
    val slot: Int?,
    val representation: GuiRepresentationBlueprint?,
)