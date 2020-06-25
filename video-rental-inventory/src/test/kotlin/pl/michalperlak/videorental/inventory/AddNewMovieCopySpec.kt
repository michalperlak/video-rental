package pl.michalperlak.videorental.inventory

import arrow.core.getOrElse
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSingleElement
import io.kotest.matchers.shouldNot
import io.kotest.matchers.string.beBlank
import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.Assumptions.assumeTrue
import pl.michalperlak.videorental.inventory.dto.NewMovie
import pl.michalperlak.videorental.inventory.dto.NewMovieCopy
import java.time.LocalDate
import java.time.Month

class AddNewMovieCopySpec : StringSpec({

    "should add movie copy and return its data" {
        // given
        val inventory = createInventory()
        val movieId = inventory.addMovie(title = "Test", releaseDate = LocalDate.of(2020, Month.JUNE, 25))
        val newMovieCopy = NewMovieCopy(movieId)

        // when
        val result = inventory.addNewCopy(newMovieCopy)

        // then
        result shouldBeRight {
            it.copyId shouldNot beBlank()
            it.movieId shouldNot beBlank()
        }
    }

    "should save copy in the repository" {
        // given
        val movieCopiesRepository = InMemoryMovieCopiesRepository()
        val inventory = createInventory(movieCopiesRepository = movieCopiesRepository)
        val movieId = inventory.addMovie(title = "Test movie", releaseDate = LocalDate.of(2020, Month.JUNE, 25))
        val newMovieCopy = NewMovieCopy(movieId)

        // when
        inventory.addNewCopy(newMovieCopy)

        // then
        val allRepoCopies = movieCopiesRepository.getAll()
        allRepoCopies shouldHaveSingleElement {
            it.movieId.toString() == movieId
        }
    }
})

private fun InventoryFacade.addMovie(title: String, releaseDate: LocalDate): String {
    val addMovieResult = addMovie(NewMovie(title, releaseDate))
    assumeTrue(addMovieResult.isRight())
    return addMovieResult
        .getOrElse { throw IllegalStateException() }
        .id
}
