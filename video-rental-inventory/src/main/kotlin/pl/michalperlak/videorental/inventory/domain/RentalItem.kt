package pl.michalperlak.videorental.inventory.domain

internal data class RentalItem(
    val movieId: MovieId,
    val copies: Int
)