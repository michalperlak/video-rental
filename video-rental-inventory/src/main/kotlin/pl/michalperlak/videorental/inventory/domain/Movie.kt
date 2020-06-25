package pl.michalperlak.videorental.inventory.domain

import java.time.LocalDate

internal data class Movie(
    val id: MovieId,
    val title: String,
    val releaseDate: LocalDate
)
