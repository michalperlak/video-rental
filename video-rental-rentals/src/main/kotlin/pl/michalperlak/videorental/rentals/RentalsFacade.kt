package pl.michalperlak.videorental.rentals

import arrow.core.Either
import pl.michalperlak.videorental.common.util.executeForEither
import pl.michalperlak.videorental.inventory.Inventory
import pl.michalperlak.videorental.inventory.error.CopiesNotAvailable
import pl.michalperlak.videorental.inventory.error.ErrorRentingCopies
import pl.michalperlak.videorental.rentals.application.RentalService
import pl.michalperlak.videorental.rentals.dto.NewRental
import pl.michalperlak.videorental.rentals.error.ErrorCreatingRental
import pl.michalperlak.videorental.rentals.error.InventoryError
import pl.michalperlak.videorental.rentals.error.MovieNotAvailable
import pl.michalperlak.videorental.rentals.error.RentalCreationError
import pl.michalperlak.videorental.rentals.mapper.asDto
import pl.michalperlak.videorental.rentals.mapper.forInventory
import pl.michalperlak.videorental.rentals.dto.Rental as RentalDto

internal class RentalsFacade(
    private val inventory: Inventory,
    private val rentalService: RentalService
) : Rentals {

    override fun newRental(rental: NewRental): Either<RentalCreationError, RentalDto> =
        executeForEither({
            inventory
                .rentMovies(rental.items.map { it.forInventory() })
                .map { rentalService.registerRental(rental, it) }
                .map { it.asDto() }
                .mapLeft {
                    when (it) {
                        is CopiesNotAvailable -> MovieNotAvailable(it.movieId)
                        is ErrorRentingCopies -> InventoryError(it.error)
                    }
                }
        }, errorHandler = { ErrorCreatingRental(it) })
}