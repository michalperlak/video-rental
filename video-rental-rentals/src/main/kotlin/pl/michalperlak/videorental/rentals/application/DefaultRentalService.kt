package pl.michalperlak.videorental.rentals.application

import pl.michalperlak.videorental.inventory.dto.RentedCopy
import pl.michalperlak.videorental.pricing.Prices
import pl.michalperlak.videorental.pricing.classification.MovieClassificationPolicy
import pl.michalperlak.videorental.rentals.domain.Rental
import pl.michalperlak.videorental.rentals.domain.RentalId
import pl.michalperlak.videorental.rentals.domain.RentalItem
import pl.michalperlak.videorental.rentals.domain.RentalsRepository
import pl.michalperlak.videorental.rentals.dto.NewRental
import java.time.Clock
import java.time.Duration
import java.time.LocalDate
import pl.michalperlak.videorental.inventory.dto.Rental as InventoryRental
import pl.michalperlak.videorental.pricing.api.RentalItem as PricingRentalItem

internal class DefaultRentalService(
    private val prices: Prices,
    private val classificationPolicy: MovieClassificationPolicy,
    private val rentalsRepository: RentalsRepository,
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
                        duration = duration,
                        price = price
                    )
                }
        )
        return rentalsRepository.addRental(rental)
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
}