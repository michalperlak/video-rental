package pl.michalperlak.videorental.inventory.dto

import java.time.LocalDate

data class RentedCopy(
    val copyId: String,
    val movieId: String,
    val movieReleaseDate: LocalDate
)