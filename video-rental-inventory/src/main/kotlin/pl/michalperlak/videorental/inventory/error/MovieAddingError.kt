package pl.michalperlak.videorental.inventory.error

sealed class MovieAddingError

data class MovieAlreadyExists(
    val movieId: String
) : MovieAddingError()

data class ErrorAddingMovie(
    val error: Throwable
) : MovieAddingError()