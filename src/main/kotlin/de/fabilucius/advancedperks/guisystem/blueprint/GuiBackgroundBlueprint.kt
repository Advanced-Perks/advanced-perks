package de.fabilucius.advancedperks.guisystem.blueprint

data class GuiBackgroundBlueprint (
    val enabled: Boolean,
    val representation: GuiRepresentationBlueprint?,
) {

    init {
        //TODO for every blueprint class
        require(!enabled || representation != null) {
            "the gui background is set to enabled but is missing its representation"
        }
    }

}