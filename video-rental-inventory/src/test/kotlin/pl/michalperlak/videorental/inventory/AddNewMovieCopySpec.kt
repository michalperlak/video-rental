package pl.michalperlak.videorental.inventory

import arrow.core.getOrElse
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSingleElement
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.string.beBlank
import org.junit.jupiter.api.Assumptions.assumeTrue
import pl.michalperlak.videorental.inventory.domain.MovieId
import pl.michalperlak.videorental.inventory.dto.NewMovie
import pl.michalperlak.videorental.inventory.dto.NewMovieCopy
import pl.michalperlak.videorental.inventory.error.ErrorAddingMovieCopy
import pl.michalperlak.videorental.inventory.error.InvalidMovieId
import pl.michalperlak.videorental.inventory.error.MovieIdNotFound
import java.io.IOException
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.Month
import java.time.ZoneOffset

class AddNewMovieCopySpec : StringSpec({

    "should add movie copy and return its data" {
        // given
        val currentTime = Instant.ofEpochSecond(12345678)
        val inventory = createInventory(clock = Clock.fixed(currentTime, ZoneOffset.UTC))
        val movieId = inventory.addMovie(title = "Test", releaseDate = LocalDate.of(2020, Month.JUNE, 25))
        val newMovieCopy = NewMovieCopy(movieId)

        // when
        val result = inventory.addNewCopy(newMovieCopy)

        // then
        result shouldBeRight {
            it.copyId shouldNot beBlank()
            it.movieId shouldNot beBlank()
            it.added shouldBe currentTime
        }
    }

    "should save copy in the repository" {
        // given
        val currentTime = Instant.ofEpochSecond(12345678)
        val movieCopiesRepository = InMemoryMovieCopiesRepository()
        val inventory = createInventory(
            movieCopiesRepository = movieCopiesRepository,
            clock = Clock.fixed(currentTime, ZoneOffset.UTC))
        val movieId = inventory.addMovie(title = "Test movie", releaseDate = LocalDate.of(2020, Month.JUNE, 25))
        val newMovieCopy = NewMovieCopy(movieId)

        // when
        inventory.addNewCopy(newMovieCopy)

        // then
        val allRepoCopies = movieCopiesRepository.getAll()
        allRepoCopies shouldHaveSingleElement {
            it.movieId.toString() == movieId && it.added == currentTime
        }
    }

    "should return error when movie id is incorrect" {
        // given
        val inventory = createInventory()
        val movieId = "123456"
        val newMovieCopy = NewMovieCopy(movieId)

        // when
        val result = inventory.addNewCopy(newMovieCopy)

        // then
        result shouldBeLeft InvalidMovieId(movieId)
    }

    "should return error when movie id does not exist" {
        // given
        val inventory = createInventory()
        val movieId = MovieId.generate().toString()
        val newMovieCopy = NewMovieCopy(movieId)

        // when
        val result = inventory.addNewCopy(newMovieCopy)

        // then
        result shouldBeLeft MovieIdNotFound(movieId)
    }

    "should return error when cannot save copy in the repo" {
        // given
        val error = IOException("Infrastructure error")
        val failingRepository = FailingMovieCopiesRepository(errorProducer = { error })
        val inventory = createInventory(movieCopiesRepository = failingRepository)
        val movieId = inventory.addMovie(title = "Test movie", releaseDate = LocalDate.of(2020, Month.JUNE, 25))
        val newMovieCopy = NewMovieCopy(movieId)

        // when
        val result = inventory.addNewCopy(newMovieCopy)

        // then
        result shouldBeLeft ErrorAddingMovieCopy(error)
    }

    "should return error when cannot find the movie by its id" {
        // given
        val error = RuntimeException("Movies repo error")
        val failingMoviesRepo = FailingMoviesRepository(errorProducer = { error })
        val inventory = createInventory(moviesRepository = failingMoviesRepo)
        val movieId = MovieId.generate().toString()
        val newMovieCopy = NewMovieCopy(movieId)

        // when
        val result = inventory.addNewCopy(newMovieCopy)

        // then
        result shouldBeLeft ErrorAddingMovieCopy(error)
    }
})

private fun InventoryFacade.addMovie(title: String, releaseDate: LocalDate): String {
    val addMovieResult = addMovie(NewMovie(title, releaseDate))
    assumeTrue(addMovieResult.isRight())
    return addMovieResult
        .getOrElse { throw IllegalStateException() }
        .id
}
