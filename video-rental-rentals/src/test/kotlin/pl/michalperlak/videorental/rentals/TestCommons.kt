package pl.michalperlak.videorental.rentals

import pl.michalperlak.videorental.inventory.Inventory
import pl.michalperlak.videorental.pricing.classification.MovieClassificationPolicy
import pl.michalperlak.videorental.pricing.Prices
import pl.michalperlak.videorental.pricing.api.Price
import pl.michalperlak.videorental.rentals.application.DefaultRentalService
import pl.michalperlak.videorental.rentals.application.RentalService
import pl.michalperlak.videorental.rentals.domain.RentalsRepository
import java.time.Clock
import java.time.Period

val PREMIUM_BASE = Price.of(40)
val REGULAR_BASE = Price.of(30)

val NEW_RELEASE_MAX_AGE: Period = Period.ofDays(30)
val OLD_MOVIE_MIN_AGE: Period = Period.ofYears(20)

internal fun createRentalsService(
    prices: Prices = Prices.createInstance(PREMIUM_BASE, REGULAR_BASE),
    rentalsRepository: RentalsRepository = InMemoryRentalsRepository(),
    clock: Clock = Clock.systemUTC(),
    classificationPolicy: MovieClassificationPolicy = MovieClassificationPolicy.default(
        NEW_RELEASE_MAX_AGE,
        OLD_MOVIE_MIN_AGE,
        clock
    )
): RentalService = DefaultRentalService(prices, classificationPolicy, clock)

internal fun createRentals(
    inventory: Inventory,
    rentalsService: RentalService = createRentalsService()
): Rentals =
    RentalsFacade(inventory, rentalsService)