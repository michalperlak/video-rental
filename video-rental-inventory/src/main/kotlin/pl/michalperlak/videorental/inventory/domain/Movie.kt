package pl.michalperlak.videorental.inventory.domain

import pl.michalperlak.videorental.inventory.MovieId
import java.time.LocalDate

data class Movie(
    val id: MovieId,
    val title: String,
    val releaseDate: LocalDate
)
