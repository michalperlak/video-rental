package pl.michalperlak.videorental.rentals

import arrow.core.Either
import arrow.core.ListK
import arrow.core.getOrElse
import arrow.core.k
import io.mockk.every
import org.junit.jupiter.api.Assumptions.assumeTrue
import pl.michalperlak.videorental.common.events.Events
import pl.michalperlak.videorental.common.events.infr.InMemoryEventBus
import pl.michalperlak.videorental.inventory.Inventory
import pl.michalperlak.videorental.inventory.dto.Rental
import pl.michalperlak.videorental.inventory.dto.RentedCopy
import pl.michalperlak.videorental.pricing.Prices
import pl.michalperlak.videorental.pricing.api.Price
import pl.michalperlak.videorental.pricing.classification.MovieClassificationPolicy
import pl.michalperlak.videorental.rentals.application.DefaultRentalService
import pl.michalperlak.videorental.rentals.application.RentalService
import pl.michalperlak.videorental.rentals.domain.RentalsRepository
import pl.michalperlak.videorental.rentals.domain.ReturnsRepository
import pl.michalperlak.videorental.rentals.dto.NewRental
import pl.michalperlak.videorental.rentals.dto.NewRentalItem
import pl.michalperlak.videorental.rentals.infr.InMemoryRentalsRepository
import pl.michalperlak.videorental.rentals.infr.InMemoryReturnsRepository
import java.time.Clock
import java.time.Duration
import java.time.LocalDate
import java.time.Month
import java.time.Period
import java.util.UUID

val PREMIUM_BASE = Price.of(40)
val REGULAR_BASE = Price.of(30)

val NEW_RELEASE_MAX_AGE: Period = Period.ofDays(30)
val OLD_MOVIE_MIN_AGE: Period = Period.ofYears(20)

internal fun createRentalsService(
    prices: Prices = Prices.createInstance(PREMIUM_BASE, REGULAR_BASE),
    rentalsRepository: RentalsRepository = InMemoryRentalsRepository(),
    returnsRepository: ReturnsRepository = InMemoryReturnsRepository(),
    clock: Clock = Clock.systemUTC(),
    classificationPolicy: MovieClassificationPolicy = MovieClassificationPolicy.default(
        NEW_RELEASE_MAX_AGE,
        OLD_MOVIE_MIN_AGE,
        clock
    )
): RentalService = DefaultRentalService(prices, classificationPolicy, rentalsRepository, returnsRepository, clock)

internal fun createRentals(
    inventory: Inventory,
    clock: Clock = Clock.systemUTC(),
    rentalsService: RentalService = createRentalsService(clock = clock),
    events: Events = InMemoryEventBus()
): Rentals =
    RentalsFacade(inventory, rentalsService, events, clock)

internal fun addRental(
    copies: ListK<String>, inventory: Inventory,
    rentals: Rentals, duration: Duration = Duration.ofDays(2)
): String {
    val rentalItem = NewRentalItem(createMovieId(), duration)
    every { inventory.rentMovies(any()) } returns Either.right(
        Rental(
            copies = copies.map {
                RentedCopy(
                    copyId = it,
                    movieId = rentalItem.movieId,
                    movieReleaseDate = LocalDate.of(2019, Month.NOVEMBER, 13)
                )
            }
        )
    )
    val rental = NewRental(createCustomerId(), listOf(rentalItem).k())
    val result = rentals.newRental(rental)
    assumeTrue(result.isRight())
    return result
        .getOrElse { throw IllegalStateException() }
        .rentalId
}

internal fun createCustomerId(): String = UUID.randomUUID().toString()

internal fun createMovieId(): String = UUID.randomUUID().toString()

internal fun createCopyId(): String = UUID.randomUUID().toString()