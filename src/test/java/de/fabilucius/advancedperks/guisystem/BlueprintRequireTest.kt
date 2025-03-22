package de.fabilucius.advancedperks.guisystem

import de.fabilucius.advancedperks.guisystem.blueprint.GuiBackgroundBlueprint
import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Test

class BlueprintRequireTest {

    @Test
    fun `check if blueprint generation fails if enabled is true but mandatory fields are missing`() {
        val validBlueprint = GuiBackgroundBlueprint(false, null)
        shouldThrow<IllegalArgumentException> {
            GuiBackgroundBlueprint(true, null)
        }
    }

}