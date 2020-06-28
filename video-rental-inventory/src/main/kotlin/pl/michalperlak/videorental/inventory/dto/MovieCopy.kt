package pl.michalperlak.videorental.inventory.dto

import java.time.Instant

data class MovieCopy(
    val copyId: String,
    val movieId: String,
    val added: Instant,
    val status: String
)
