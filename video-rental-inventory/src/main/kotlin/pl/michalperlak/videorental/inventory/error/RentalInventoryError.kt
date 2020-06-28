package pl.michalperlak.videorental.inventory.error

sealed class RentalInventoryError

data class CopiesNotAvailable(
    val movieId: String
) : RentalInventoryError()

data class ErrorRentingCopies(
    val error: Throwable
) : RentalInventoryError()