package pl.michalperlak.videorental.rentals

import arrow.core.Either
import arrow.core.ListK.Companion.empty
import arrow.core.k
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSingleElement
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import pl.michalperlak.videorental.inventory.Inventory
import pl.michalperlak.videorental.inventory.error.ErrorReturningCopies
import pl.michalperlak.videorental.inventory.error.UnknownCopies
import pl.michalperlak.videorental.rentals.domain.RentalId
import pl.michalperlak.videorental.rentals.error.CopiesNotRecognized
import pl.michalperlak.videorental.rentals.error.ErrorDuringReturn
import pl.michalperlak.videorental.rentals.error.RentalNotRecognized
import pl.michalperlak.videorental.rentals.infr.InMemoryReturnsRepository
import java.io.IOException
import java.time.Duration
import java.time.LocalDate
import java.time.Month

class ReturnCopiesSpec : StringSpec({

    "should return error when rental id is not correct" {
        // given
        val inventory = mockk<Inventory>()
        val rentals = createRentals(inventory)
        val rentalId = "abcdef"

        // when
        val result = rentals.returnCopies(rentalId, empty())

        // then
        result shouldBeLeft RentalNotRecognized(rentalId)
    }

    "should return error when rental for id has not been found" {
        // given
        val inventory = mockk<Inventory>()
        val rentals = createRentals(inventory)
        val rentalId = RentalId.generate().toString()

        // when
        val result = rentals.returnCopies(rentalId, empty())

        // then
        result shouldBeLeft RentalNotRecognized(rentalId)
    }

    "should return error when cannot return copies in the inventory" {
        // given
        val inventory = mockk<Inventory>()
        val rentals = createRentals(inventory)
        val copies = listOf(createCopyId()).k()
        every { inventory.returnCopies(any()) } returns Either.left(UnknownCopies(copies))
        val rentalId = addRental(copies, inventory, rentals)

        // when
        val result = rentals.returnCopies(rentalId, copies)

        // then
        result shouldBeLeft CopiesNotRecognized(copies)
    }

    "should return error when inventory error occurred" {
        // given
        val error = RuntimeException("Inventory error")
        val inventory = mockk<Inventory>()
        every { inventory.returnCopies(any()) } returns Either.left(ErrorReturningCopies(error))
        val rentals = createRentals(inventory)
        val copies = listOf(createCopyId()).k()
        val rentalId = addRental(copies, inventory, rentals)

        // when
        val result = rentals.returnCopies(rentalId, copies)

        // then
        result shouldBeLeft ErrorDuringReturn(error)
    }

    "should return data about returned copies and additional charges when return successful" {
        // given
        val inventory = mockk<Inventory>()
        every { inventory.returnCopies(any()) } returns Either.right(Unit)
        val rentalDate = LocalDate.of(2020, Month.JUNE, 20)
        val clock = MutableClock(rentalDate)
        val rentals = createRentals(inventory, createRentalsService(clock = clock))
        val copyId = createCopyId()
        val copies = listOf(copyId).k()
        val rentalId = addRental(copies, inventory, rentals, Duration.ofDays(3))
        val returnDate = LocalDate.of(2020, Month.JUNE, 25)
        clock.currentDate = returnDate

        // when
        val result = rentals.returnCopies(rentalId, copies)

        // then
        result shouldBeRight {
            it.returnDate shouldBe returnDate
            it.items shouldHaveSingleElement { copy ->
                copy.copyId == copyId && copy.delay == Duration.ofDays(2)
            }
        }
    }

    "should save return data in the repository" {
        // given
        val returnsRepo = InMemoryReturnsRepository()
        val inventory = mockk<Inventory>()
        every { inventory.returnCopies(any()) } returns Either.right(Unit)
        val currentDate = LocalDate.of(2020, Month.JUNE, 29)
        val rentals = createRentals(inventory, createRentalsService(
            returnsRepository = returnsRepo, clock = MutableClock(currentDate)))
        val copyId = createCopyId()
        val copies = listOf(copyId).k()
        val rentalId = addRental(copies, inventory, rentals)

        // when
        rentals.returnCopies(rentalId, copies)

        // then
        val allRepoReturns = returnsRepo.getAll()
        allRepoReturns shouldHaveSingleElement {
            it.returnDate == currentDate && it.items.size == 1 && it.items[0].copyId == copyId
        }
    }

    "should return error when cannot save return in the repository" {
        // given
        val error = IOException("Repository error")
        val failingRepo = FailingReturnsRepository { error }
        val inventory = mockk<Inventory>()
        val rentals = createRentals(inventory, createRentalsService(returnsRepository = failingRepo))
        val copies = listOf(createCopyId()).k()
        val rentalId = addRental(copies, inventory, rentals)

        // when
        val result = rentals.returnCopies(rentalId, copies)

        // then
        result shouldBeLeft ErrorDuringReturn(error)
    }
})