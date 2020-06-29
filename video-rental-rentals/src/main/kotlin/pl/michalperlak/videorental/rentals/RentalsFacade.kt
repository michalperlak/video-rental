package pl.michalperlak.videorental.rentals

import arrow.core.Either
import pl.michalperlak.videorental.inventory.Inventory
import pl.michalperlak.videorental.inventory.error.CopiesNotAvailable
import pl.michalperlak.videorental.inventory.error.ErrorRentingCopies
import pl.michalperlak.videorental.rentals.dto.NewRental
import pl.michalperlak.videorental.rentals.dto.Rental
import pl.michalperlak.videorental.rentals.error.InventoryError
import pl.michalperlak.videorental.rentals.error.MovieNotAvailable
import pl.michalperlak.videorental.rentals.error.RentalCreationError
import pl.michalperlak.videorental.rentals.mapper.forInventory

internal class RentalsFacade(
    private val inventory: Inventory
) : Rentals {

    override fun newRental(rental: NewRental): Either<RentalCreationError, Rental> {
        return inventory
            .rentMovies(rental.items.map { it.forInventory() })
            .map { TODO() }
            .mapLeft {
                when (it) {
                    is CopiesNotAvailable -> MovieNotAvailable(it.movieId)
                    is ErrorRentingCopies -> InventoryError(it.error)
                }
            }
    }
}