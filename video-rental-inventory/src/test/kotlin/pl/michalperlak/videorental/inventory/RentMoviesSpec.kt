package pl.michalperlak.videorental.inventory

import arrow.core.getOrElse
import arrow.core.k
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import pl.michalperlak.videorental.inventory.domain.MovieCopyStatus
import pl.michalperlak.videorental.inventory.domain.MovieId
import pl.michalperlak.videorental.inventory.dto.MovieToRent
import pl.michalperlak.videorental.inventory.dto.RentedCopy
import pl.michalperlak.videorental.inventory.error.CopiesNotAvailable
import pl.michalperlak.videorental.inventory.error.ErrorRentingCopies
import java.io.IOException
import java.time.LocalDate
import java.time.Month

class RentMoviesSpec : StringSpec({

    "should return error with not available movies when there are no copies for rent" {
        // given
        val inventory = createInventory()
        val movieId = inventory.addMovie("Test movie", LocalDate.of(2020, Month.JUNE, 27))
        val moviesToRent = listOf(MovieToRent(movieId = movieId, copies = 1)).k()

        // when
        val result = inventory.rentMovies(moviesToRent)

        // then
        result shouldBeLeft CopiesNotAvailable(movieId)
    }

    "should rent requested movies and return the rental data" {
        // given
        val inventory = createInventory()
        val movieId = inventory.addMovie("Test movie 2", LocalDate.of(2020, Month.JUNE, 27))
        val copyId1 = inventory.addNewCopy(movieId)
        val copyId2 = inventory.addNewCopy(movieId)
        val moviesToRent = listOf(MovieToRent(movieId = movieId, copies = 2)).k()

        // when
        val result = inventory.rentMovies(moviesToRent)

        // then
        result shouldBeRight {
            it.copies shouldHaveSize 2
            it.copies shouldContainAll listOf(
                RentedCopy(copyId = copyId1, movieId = movieId), RentedCopy(copyId = copyId2, movieId = movieId)
            )
        }
    }

    "should mark rented copies as not available" {
        // given
        val inventory = createInventory()
        val movieId = inventory.addMovie("Test movie", LocalDate.of(2020, Month.JUNE, 27))
        val copyId = inventory.addNewCopy(movieId)
        val moviesToRent = listOf(MovieToRent(movieId = movieId, copies = 1)).k()

        // when
        inventory.rentMovies(moviesToRent)

        // then
        val copy = inventory
            .getCopy(copyId)
            .getOrElse { throw AssertionError("Copy not found: $copyId") }
        copy.status shouldBe MovieCopyStatus.RENTED.name
    }

    "should return error when cannot find movie copy due to internal error" {
        // given
        val error = IOException("Infrastructure error")
        val movieCopiesRepository = FailingMovieCopiesRepository { error }
        val inventory = createInventory(movieCopiesRepository = movieCopiesRepository)
        val moviesToRent = listOf(MovieToRent(movieId = MovieId.generate().toString(), copies = 1)).k()

        // when
        val result = inventory.rentMovies(moviesToRent)

        // then
        result shouldBeLeft ErrorRentingCopies(error)
    }
})