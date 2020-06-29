package pl.michalperlak.videorental.rentals

import arrow.core.Either
import arrow.core.k
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import pl.michalperlak.videorental.inventory.Inventory
import pl.michalperlak.videorental.inventory.error.CopiesNotAvailable
import pl.michalperlak.videorental.inventory.error.ErrorRentingCopies
import pl.michalperlak.videorental.rentals.dto.NewRental
import pl.michalperlak.videorental.rentals.dto.NewRentalItem
import pl.michalperlak.videorental.rentals.error.InventoryError
import pl.michalperlak.videorental.rentals.error.MovieNotAvailable
import java.time.Duration
import java.util.UUID

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
})

private fun createMovieId(): String = UUID.randomUUID().toString()