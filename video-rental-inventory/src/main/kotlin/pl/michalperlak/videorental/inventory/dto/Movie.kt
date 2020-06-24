package pl.michalperlak.videorental.inventory.dto

import java.time.LocalDate

data class Movie(
    val id: String,
    val title: String,
    val releaseDate: LocalDate
)