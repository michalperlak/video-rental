package pl.michalperlak.videorental.inventory.dto

import java.time.LocalDate

data class NewMovie(
    val title: String,
    val releaseDate: LocalDate
)