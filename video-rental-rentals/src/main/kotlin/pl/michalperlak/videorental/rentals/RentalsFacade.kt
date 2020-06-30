package pl.michalperlak.videorental.rentals

import arrow.core.Either
import arrow.core.ListK
import arrow.core.flatMap
import pl.michalperlak.videorental.common.events.Events
import pl.michalperlak.videorental.common.events.rentals.NewRentalCreated
import pl.michalperlak.videorental.common.util.executeForEither
import pl.michalperlak.videorental.inventory.Inventory
import pl.michalperlak.videorental.inventory.error.CopiesNotAvailable
import pl.michalperlak.videorental.inventory.error.ErrorRentingCopies
import pl.michalperlak.videorental.inventory.error.ErrorReturningCopies
import pl.michalperlak.videorental.inventory.error.UnknownCopies
import pl.michalperlak.videorental.rentals.application.RentalService
import pl.michalperlak.videorental.rentals.domain.RentalId
import pl.michalperlak.videorental.rentals.dto.CopiesReturn
import pl.michalperlak.videorental.rentals.dto.NewRental
import pl.michalperlak.videorental.rentals.dto.RentedMovieCopy
import pl.michalperlak.videorental.rentals.error.CopiesNotRecognized
import pl.michalperlak.videorental.rentals.error.CopiesReturnError
import pl.michalperlak.videorental.rentals.error.ErrorCreatingRental
import pl.michalperlak.videorental.rentals.error.ErrorDuringReturn
import pl.michalperlak.videorental.rentals.error.InventoryError
import pl.michalperlak.videorental.rentals.error.MovieNotAvailable
import pl.michalperlak.videorental.rentals.error.RentalCreationError
import pl.michalperlak.videorental.rentals.error.RentalNotFoundException
import pl.michalperlak.videorental.rentals.error.RentalNotRecognized
import pl.michalperlak.videorental.rentals.mapper.asDto
import pl.michalperlak.videorental.rentals.mapper.forInventory
import java.time.Clock
import java.time.Instant
import pl.michalperlak.videorental.rentals.dto.Rental as RentalDto

internal class RentalsFacade(
    private val inventory: Inventory,
    private val rentalService: RentalService,
    private val events: Events,
    private val clock: Clock
) : Rentals {

    override fun newRental(rental: NewRental): Either<RentalCreationError, RentalDto> =
        executeForEither({
            inventory
                .rentMovies(rental.items.map { it.forInventory() })
                .map { rentalService.registerRental(rental, it) }
                .map { publishEvent(it.asDto()) }
                .mapLeft {
                    when (it) {
                        is CopiesNotAvailable -> MovieNotAvailable(it.movieId)
                        is ErrorRentingCopies -> InventoryError(it.error)
                    }
                }
        }, errorHandler = { ErrorCreatingRental(it) })

    override fun returnCopies(rentalId: String, movieCopyIds: ListK<String>): Either<CopiesReturnError, CopiesReturn> =
        executeForEither({
            RentalId
                .from(rentalId)
                .map { rentalService.returnCopies(it, movieCopyIds) }
                .toEither { RentalNotRecognized(rentalId) }
                .flatMap { copiesReturn -> returnToInventory(movieCopyIds).map { copiesReturn } }
                .map { it.asDto() }
        }, errorHandler = {
            when (it) {
                is RentalNotFoundException -> RentalNotRecognized(it.rentalId.toString())
                else -> ErrorDuringReturn(it.apply { printStackTrace() })
            }
        })

    private fun returnToInventory(movieCopyIds: ListK<String>): Either<CopiesReturnError, Unit> =
        inventory
            .returnCopies(movieCopyIds)
            .mapLeft {
                when (it) {
                    is UnknownCopies -> CopiesNotRecognized(it.copyIds)
                    is ErrorReturningCopies -> ErrorDuringReturn(it.error)
                }
            }

    private fun publishEvent(rental: RentalDto): RentalDto = rental.apply {
        events.publish(
            NewRentalCreated(rental.customerId, items.map(RentedMovieCopy::type), Instant.now(clock))
        )
    }
}