package de.fabilucius.advancedperks.core.gui.blueprint

data class GuiWindowBlueprint(
    val title: String,
    val size: Int,
    val pages: List<GuiPageBlueprint>
)
