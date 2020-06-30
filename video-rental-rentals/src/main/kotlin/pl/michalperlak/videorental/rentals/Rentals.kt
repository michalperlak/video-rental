package pl.michalperlak.videorental.rentals

import arrow.core.Either
import arrow.core.ListK
import pl.michalperlak.videorental.common.events.Events
import pl.michalperlak.videorental.inventory.Inventory
import pl.michalperlak.videorental.pricing.Prices
import pl.michalperlak.videorental.pricing.classification.MovieClassificationPolicy
import pl.michalperlak.videorental.rentals.application.DefaultRentalService
import pl.michalperlak.videorental.rentals.dto.CopiesReturn
import pl.michalperlak.videorental.rentals.dto.NewRental
import pl.michalperlak.videorental.rentals.dto.Rental
import pl.michalperlak.videorental.rentals.error.CopiesReturnError
import pl.michalperlak.videorental.rentals.error.RentalCreationError
import pl.michalperlak.videorental.rentals.infr.InMemoryRentalsRepository
import pl.michalperlak.videorental.rentals.infr.InMemoryReturnsRepository
import java.time.Clock

interface Rentals {
    fun newRental(rental: NewRental): Either<RentalCreationError, Rental>
    fun returnCopies(rentalId: String, movieCopyIds: ListK<String>): Either<CopiesReturnError, CopiesReturn>

    companion object {
        fun create(
            prices: Prices,
            movieClassificationPolicy: MovieClassificationPolicy,
            inventory: Inventory,
            events: Events,
            clock: Clock
        ): Rentals =
            RentalsFacade(
                inventory = inventory,
                rentalService = DefaultRentalService(
                    prices = prices,
                    classificationPolicy = movieClassificationPolicy,
                    returnsRepository = InMemoryReturnsRepository(),
                    rentalsRepository = InMemoryRentalsRepository(),
                    clock = clock
                ),
                events = events,
                clock = clock
            )
    }
}