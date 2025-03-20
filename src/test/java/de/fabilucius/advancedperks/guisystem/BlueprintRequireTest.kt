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

    @Test
    fun lol() {
        for(i in 0..10) {
            test(3, i)
        }
    }

    private fun test(perSize: Int, page: Int) {
        val list = listOf("HI", "ELLO", "SHEESH", "ESEL", "MATCH", "FORK")

        val minBound = perSize.times(page).coerceAtLeast(0)
        val maxBound = minBound.plus(perSize).coerceAtMost(list.size)

        val nextPageAvailable = list.size > perSize.times(page).plus(perSize)
        println("$page $nextPageAvailable")
    }
}