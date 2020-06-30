package pl.michalperlak.videorental.inventory

import arrow.core.k
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import pl.michalperlak.videorental.inventory.domain.MovieCopy
import pl.michalperlak.videorental.inventory.domain.MovieCopyId
import pl.michalperlak.videorental.inventory.domain.MovieCopyStatus
import pl.michalperlak.videorental.inventory.dto.MovieToRent
import pl.michalperlak.videorental.inventory.error.ErrorReturningCopies
import pl.michalperlak.videorental.inventory.error.UnknownCopies
import java.time.LocalDate
import java.time.Month

class ReturnCopiesSpec : StringSpec({

    "should return error when copy is not recognized" {
        // given
        val inventory = createInventory()
        val copyId = MovieCopyId.generate().toString()

        // when
        val result = inventory.returnCopies(listOf(copyId).k())

        // then
        result shouldBeLeft UnknownCopies(listOf(copyId).k())
    }

    "should return error when copies repo is failing" {
        // given
        val error = RuntimeException("Repository exception")
        val inventory = createInventory(movieCopiesRepository = FailingMovieCopiesRepository { error })
        val copyId = MovieCopyId.generate().toString()

        // when
        val result = inventory.returnCopies(listOf(copyId).k())

        // then
        result shouldBeLeft ErrorReturningCopies(error)
    }

    "should return success when copies returned" {
        // given
        val inventory = createInventory()
        val movieId = inventory.addMovie("Test movie", LocalDate.of(2020, Month.JUNE, 29))
        val copyId = inventory.addNewCopy(movieId)

        // when
        val result = inventory.returnCopies(listOf(copyId).k())

        // then
        result shouldBeRight Unit
    }

    "should mark returned copies as available" {
        // given
        val copiesRepo = InMemoryMovieCopiesRepository()
        val inventory = createInventory(movieCopiesRepository = copiesRepo)
        val movieId = inventory.addMovie("Test movie", LocalDate.of(2020, Month.JUNE, 29))
        val copy1 = inventory.addNewCopy(movieId)
        val copy2 = inventory.addNewCopy(movieId)
        inventory.rentMovies(listOf(MovieToRent(movieId, 2)).k())

        // when
        inventory.returnCopies(listOf(copy1, copy2).k())

        // then
        val allCopies = copiesRepo.getAll()
        allCopies.filter { it.isAvailableCopyWithIdIn(copy1, copy2) } shouldHaveSize 2
    }
})

private fun MovieCopy.isAvailableCopyWithIdIn(vararg copies: String): Boolean =
    copies.contains(id.toString()) && status == MovieCopyStatus.AVAILABLE