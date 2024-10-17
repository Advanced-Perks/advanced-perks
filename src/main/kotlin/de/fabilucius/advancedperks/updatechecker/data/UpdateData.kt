package de.fabilucius.advancedperks.updatechecker.data

import java.util.Date
import java.util.UUID

data class UpdateData(
    val downloads: Int,
    val name: String,
    val rating: Rating,
    val releaseDate: Date,
    val resource: Int,
    val uuid: UUID,
    val id: Int,
)
