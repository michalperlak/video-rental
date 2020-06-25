package pl.michalperlak.videorental.inventory.domain

data class MovieCopy(
    val id: MovieCopyId,
    val movieId: MovieId
)