package pl.michalperlak.videorental.rentals

import arrow.core.Either
import arrow.core.k
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldHaveSingleElement
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import pl.michalperlak.videorental.inventory.Inventory
import pl.michalperlak.videorental.inventory.dto.Rental
import pl.michalperlak.videorental.inventory.dto.RentedCopy
import pl.michalperlak.videorental.inventory.error.CopiesNotAvailable
import pl.michalperlak.videorental.inventory.error.ErrorRentingCopies
import pl.michalperlak.videorental.pricing.api.Price
import pl.michalperlak.videorental.rentals.dto.NewRental
import pl.michalperlak.videorental.rentals.dto.NewRentalItem
import pl.michalperlak.videorental.rentals.dto.RentedMovieCopy
import pl.michalperlak.videorental.rentals.error.ErrorCreatingRental
import pl.michalperlak.videorental.rentals.error.InventoryError
import pl.michalperlak.videorental.rentals.error.MovieNotAvailable
import pl.michalperlak.videorental.rentals.infr.InMemoryRentalsRepository
import java.time.Clock
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneOffset

class CreateNewRentalSpec : StringSpec({

    "should return error when copies are not available in the inventory" {
        // given
        val movieId = createMovieId()
        val inventory = mockk<Inventory>()
        every { inventory.rentMovies(any()) } returns Either.left(CopiesNotAvailable(movieId))
        val rentals = createRentals(inventory)
        val rental = NewRental(
            listOf(NewRentalItem(movieId, Duration.ofDays(2))).k()
        )

        // when
        val result = rentals.newRental(rental)

        // then
        result shouldBeLeft MovieNotAvailable(movieId)
    }

    "should return error when cannot rent copies from inventory" {
        // given
        val error = RuntimeException("Inventory error")
        val movieId = createMovieId()
        val inventory = mockk<Inventory>()
        every { inventory.rentMovies(any()) } returns Either.left(ErrorRentingCopies(error))
        val rentals = createRentals(inventory)
        val rental = NewRental(
            listOf(NewRentalItem(movieId, Duration.ofDays(10))).k()
        )

        // when
        val result = rentals.newRental(rental)

        // then
        result shouldBeLeft InventoryError(error)
    }

    "should create rental with correct total price" {
        // given
        val currentTime = LocalDateTime.of(2020, Month.JUNE, 29, 0, 0)
        val inventory = mockk<Inventory>()
        val rentals = createRentals(
            inventory,
            createRentalsService(clock = Clock.fixed(currentTime.toInstant(ZoneOffset.UTC), ZoneOffset.UTC))
        )
        val oldMovieItem = NewRentalItem(createMovieId(), Duration.ofDays(5))
        val regularMovieItem = NewRentalItem(createMovieId(), Duration.ofDays(4))
        val newReleaseItem = NewRentalItem(createMovieId(), Duration.ofDays(2))
        every { inventory.rentMovies(any()) } returns Either.right(
            Rental(
                copies = listOf(
                    RentedCopy(
                        copyId = createCopyId(),
                        movieId = oldMovieItem.movieId,
                        movieReleaseDate = LocalDate.of(1990, Month.JULY, 23)
                    ),
                    RentedCopy(
                        copyId = createCopyId(),
                        movieId = regularMovieItem.movieId,
                        movieReleaseDate = LocalDate.of(2017, Month.AUGUST, 15)
                    ),
                    RentedCopy(
                        copyId = createCopyId(),
                        movieId = newReleaseItem.movieId,
                        movieReleaseDate = LocalDate.of(2020, Month.JUNE, 27)
                    )
                ).k()
            )
        )
        val rental = NewRental(listOf(oldMovieItem, regularMovieItem, newReleaseItem).k())

        // when
        val result = rentals.newRental(rental)

        // then
        result shouldBeRight {
            it.totalPrice shouldBe Price.of(170)
        }
    }

    "should create rental with correct price for each item" {
        // given
        val currentTime = LocalDateTime.of(2020, Month.JUNE, 29, 0, 0)
        val inventory = mockk<Inventory>()
        val rentals = createRentals(
            inventory,
            createRentalsService(clock = Clock.fixed(currentTime.toInstant(ZoneOffset.UTC), ZoneOffset.UTC))
        )
        val oldMovieItem = NewRentalItem(createMovieId(), Duration.ofDays(7))
        val regularMovieItem = NewRentalItem(createMovieId(), Duration.ofDays(4))
        val newReleaseItem = NewRentalItem(createMovieId(), Duration.ofDays(5))
        every { inventory.rentMovies(any()) } returns Either.right(
            Rental(
                copies = listOf(
                    RentedCopy(
                        copyId = createCopyId(),
                        movieId = oldMovieItem.movieId,
                        movieReleaseDate = LocalDate.of(1995, Month.OCTOBER, 23)
                    ),
                    RentedCopy(
                        copyId = createCopyId(),
                        movieId = regularMovieItem.movieId,
                        movieReleaseDate = LocalDate.of(2017, Month.APRIL, 6)
                    ),
                    RentedCopy(
                        copyId = createCopyId(),
                        movieId = newReleaseItem.movieId,
                        movieReleaseDate = LocalDate.of(2020, Month.JUNE, 27)
                    )
                ).k()
            )
        )

        val rental = NewRental(listOf(oldMovieItem, regularMovieItem, newReleaseItem).k())

        // when
        val result = rentals.newRental(rental)

        // then
        result shouldBeRight {
            it.items shouldHaveSize 3
            it.items.map(RentedMovieCopy::price) shouldContainAll
                    listOf(Price.of(90), Price.of(60), Price.of(200))
        }
    }

    "should save rental in the repository" {
        // given
        val rentalsRepository = InMemoryRentalsRepository()
        val currentTime = LocalDateTime.of(2020, Month.JUNE, 29, 0, 0)
        val inventory = mockk<Inventory>()
        val rentals = createRentals(
            inventory,
            createRentalsService(
                rentalsRepository = rentalsRepository,
                clock = Clock.fixed(currentTime.toInstant(ZoneOffset.UTC), ZoneOffset.UTC)
            )
        )
        val rentalItem = NewRentalItem(createMovieId(), Duration.ofDays(5))
        every { inventory.rentMovies(any()) } returns Either.right(
            Rental(
                copies = listOf(
                    RentedCopy(
                        copyId = createCopyId(),
                        movieId = rentalItem.movieId,
                        movieReleaseDate = LocalDate.of(2015, Month.JULY, 23)
                    )
                ).k()
            )
        )
        val rental = NewRental(listOf(rentalItem).k())

        // when
        rentals.newRental(rental)

        // then
        val allRepoRentals = rentalsRepository.getAll()
        allRepoRentals shouldHaveSingleElement {
            it.startDate == currentTime.toLocalDate() && it.items.size == 1
        }
    }

    "should return error when cannot save rental in the repository" {
        // given
        val error = RuntimeException("Repository error")
        val failingRepo = FailingRentalsRepository { error }
        val inventory = mockk<Inventory>()
        val rentals = createRentals(inventory, createRentalsService(rentalsRepository = failingRepo))
        val rentalItem = NewRentalItem(createMovieId(), Duration.ofDays(2))
        every { inventory.rentMovies(any()) } returns Either.right(
            Rental(
                copies = listOf(
                    RentedCopy(
                        copyId = createCopyId(),
                        movieId = rentalItem.movieId,
                        movieReleaseDate = LocalDate.of(2019, Month.NOVEMBER, 13)
                    )
                ).k()
            )
        )
        val rental = NewRental(listOf(rentalItem).k())

        // when
        val result = rentals.newRental(rental)

        // then
        result shouldBeLeft ErrorCreatingRental(error)
    }
})