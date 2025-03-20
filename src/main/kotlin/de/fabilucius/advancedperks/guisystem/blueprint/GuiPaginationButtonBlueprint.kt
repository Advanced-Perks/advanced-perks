package de.fabilucius.advancedperks.guisystem.blueprint

data class GuiPaginationButtonBlueprint(
    val previousPageButton: GuiPreviousPageButtonBlueprint,
    val nextPageButton: GuiNextPageButtonBlueprint,
)

data class GuiNextPageButtonBlueprint(
    val slot: Int,
    val hideWhenUnnecessary: Boolean,
    val representation: GuiRepresentationBlueprint,
)

data class GuiPreviousPageButtonBlueprint(
    val slot: Int,
    val hideWhenUnnecessary: Boolean,
    val representation: GuiRepresentationBlueprint,
)