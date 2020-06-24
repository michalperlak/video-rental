package pl.michalperlak.videorental.inventory

import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSingleElement
import io.kotest.matchers.shouldBe
import pl.michalperlak.videorental.inventory.dto.NewMovie
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


})
