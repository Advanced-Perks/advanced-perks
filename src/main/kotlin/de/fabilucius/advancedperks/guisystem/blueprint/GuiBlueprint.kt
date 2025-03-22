package de.fabilucius.advancedperks.guisystem.blueprint

data class GuiBlueprint(
    val title: String,
    val size: Int,
    val background: GuiBackgroundBlueprint,
    val perkButton: GuiPerkButtonBlueprint,
    val paginationButton: GuiPaginationButtonBlueprint,
    val utilityButton: GuiUtilityButtonBlueprint,
)
