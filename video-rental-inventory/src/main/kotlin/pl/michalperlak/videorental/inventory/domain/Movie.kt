package pl.michalperlak.videorental.inventory.domain

import java.time.LocalDate

data class Movie(
    val id: MovieId,
    val title: String,
    val releaseDate: LocalDate
)
