package pl.michalperlak.videorental.inventory.error

internal class CopiesNotAvailableException(
    val movieId: String,
    requestedCopies: Int
) : RuntimeException("Requested number of copies: $requestedCopies for movie id: $movieId is not available")