package pl.michalperlak.videorental.inventory

import arrow.core.getOrElse
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSingleElement
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.Assumptions.assumeTrue
import pl.michalperlak.videorental.inventory.dto.NewMovie
import pl.michalperlak.videorental.inventory.error.MovieAlreadyExists
import java.time.LocalDate
import java.time.Month

class AddNewMovieSpec : StringSpec({
    val moviesRepository = InMemoryMoviesRepository()
    val inventory = InventoryFacade(moviesRepository)

    "should add movie and return its data" {
        // given
        val title = "Test movie"
        val releaseDate = LocalDate.of(2020, Month.JUNE, 24)
        val newMovie = NewMovie(title = title, releaseDate = releaseDate)

        // when
        val result = inventory.addMovie(newMovie)

        // then
        result shouldBeRight {
            it.title shouldBe title
            it.releaseDate shouldBe releaseDate
        }
    }

    "should save the movie in the repository" {
        // given
        val title = "Movie"
        val releaseDate = LocalDate.of(2020, Month.JUNE, 24)
        val newMovie = NewMovie(title = title, releaseDate = releaseDate)

        // when
        inventory.addMovie(newMovie)

        // then
        val allRepoMovies = moviesRepository.getAll()
        allRepoMovies shouldHaveSingleElement {
            it.title == title && it.releaseDate == releaseDate
        }
    }

    "should return error when movie already registered" {
        // given
        val newMovie = NewMovie(title = "Test", releaseDate = LocalDate.of(2020, Month.JUNE, 24))
        val movieAddResult = inventory.addMovie(newMovie)
        assumeTrue(movieAddResult.isRight())
        val movieId = movieAddResult
                .getOrElse { throw IllegalStateException() }
                .id

        // when
        val result = inventory.addMovie(newMovie)

        // then
        result shouldBeLeft MovieAlreadyExists(movieId = movieId)
    }

})
