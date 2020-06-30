package pl.michalperlak.videorental.rentals.error

sealed class RentalCreationError

data class MovieNotAvailable(
    val movieId: String
) : RentalCreationError()

data class InventoryError(
    val error: Throwable
) : RentalCreationError()

data class ErrorCreatingRental(
    val error: Throwable
) : RentalCreationError()
