package pl.michalperlak.videorental.inventory.domain

import java.time.Instant

data class MovieCopy(
    val id: MovieCopyId,
    val movieId: MovieId,
    val added: Instant
)