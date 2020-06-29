package pl.michalperlak.videorental.rentals.application

import arrow.core.ListK
import arrow.core.getOrElse
import pl.michalperlak.videorental.inventory.dto.RentedCopy
import pl.michalperlak.videorental.pricing.Prices
import pl.michalperlak.videorental.pricing.api.LateReturnedItem
import pl.michalperlak.videorental.pricing.api.Price
import pl.michalperlak.videorental.pricing.classification.MovieClassificationPolicy
import pl.michalperlak.videorental.rentals.domain.Rental
import pl.michalperlak.videorental.rentals.domain.RentalId
import pl.michalperlak.videorental.rentals.domain.RentalItem
import pl.michalperlak.videorental.rentals.domain.RentalsRepository
import pl.michalperlak.videorental.rentals.domain.Return
import pl.michalperlak.videorental.rentals.domain.ReturnId
import pl.michalperlak.videorental.rentals.domain.ReturnedCopy
import pl.michalperlak.videorental.rentals.domain.ReturnsRepository
import pl.michalperlak.videorental.rentals.dto.NewRental
import pl.michalperlak.videorental.rentals.error.RentalNotFoundException
import java.time.Clock
import java.time.Duration
import java.time.LocalDate
import java.time.Period
import pl.michalperlak.videorental.inventory.dto.Rental as InventoryRental
import pl.michalperlak.videorental.pricing.api.RentalItem as PricingRentalItem

internal class DefaultRentalService(
    private val prices: Prices,
    private val classificationPolicy: MovieClassificationPolicy,
    private val rentalsRepository: RentalsRepository,
    private val returnsRepository: ReturnsRepository,
    private val clock: Clock
) : RentalService {
    override fun registerRental(rentalRequest: NewRental, inventoryRental: InventoryRental): Rental {
        val rental = Rental(
            id = RentalId.generate(),
            startDate = LocalDate.now(clock),
            items = inventoryRental
                .copies
                .map {
                    val duration = matchDuration(it, rentalRequest, inventoryRental)
                    val movieType = classificationPolicy.classify(it.movieReleaseDate)
                    val price = prices.computePrice(PricingRentalItem(movieType, duration))
                    RentalItem(
                        copyId = it.copyId,
                        movieId = it.movieId,
                        movieType = movieType,
                        duration = duration,
                        price = price
                    )
                }
        )
        return rentalsRepository.addRental(rental)
    }

    override fun returnCopies(rentalId: RentalId, movieCopyIds: ListK<String>): Return {
        val rental = rentalsRepository
            .findById(rentalId)
            .getOrElse { throw RentalNotFoundException(rentalId) }
        val returnDate = LocalDate.now(clock)
        return returnsRepository.addReturn(
            Return(
                id = ReturnId.generate(),
                returnDate = returnDate,
                items = movieCopyIds.map { returnedCopy(it, rental, returnDate) }
            )
        )
    }

    private fun matchDuration(
        rentedCopy: RentedCopy, rentalRequest: NewRental, inventoryRental: InventoryRental
    ): Duration {
        val possibleMatches = rentalRequest
            .items
            .filter { it.movieId == rentedCopy.movieId }
        val copyIndex = inventoryRental
            .copies
            .filter { it.movieId == rentedCopy.movieId }
            .indexOf(rentedCopy)
        if (copyIndex >= possibleMatches.size) {
            throw IllegalStateException("Cannot match rented copy: $rentedCopy with rental request: $rentalRequest")
        }
        return possibleMatches[copyIndex].duration
    }

    private fun returnedCopy(copyId: String, rental: Rental, returnDate: LocalDate): ReturnedCopy {
        val rentalStart = rental.startDate
        val matchedRentalItem = rental.items.first { it.copyId == copyId }
        val expectedReturnDate = rentalStart.plusDays(matchedRentalItem.duration.toDays())
        return if (expectedReturnDate.isBefore(returnDate)) {
            val delay = getDelay(returnDate, expectedReturnDate)
            val additionalCharge = prices.computeAdditionalCharges(
                LateReturnedItem(matchedRentalItem.movieType, delay)
            )
            ReturnedCopy(copyId, delay, additionalCharge)
        } else {
            ReturnedCopy(copyId, Duration.ZERO, Price.ZERO)
        }
    }

    private fun getDelay(returnDate: LocalDate, expectedReturnDate: LocalDate): Duration {
        val delayPeriod = Period.between(expectedReturnDate, returnDate)
        return Duration.ofDays(delayPeriod.days.toLong())
    }
}