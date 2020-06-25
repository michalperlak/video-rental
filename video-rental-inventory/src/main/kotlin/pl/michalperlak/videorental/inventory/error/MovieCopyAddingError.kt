package pl.michalperlak.videorental.inventory.error

sealed class MovieCopyAddingError

data class InvalidMovieId(
    val movieId: String
) : MovieCopyAddingError()