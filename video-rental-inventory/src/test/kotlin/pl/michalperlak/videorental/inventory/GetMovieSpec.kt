package pl.michalperlak.videorental.inventory

import arrow.core.extensions.option.align.empty
import io.kotest.assertions.arrow.option.shouldBeSome
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import pl.michalperlak.videorental.inventory.domain.MovieId
import java.time.LocalDate
import java.time.Month

class GetMovieSpec : StringSpec({

    "should return empty result when movie id is invalid" {
        // given
        val movieId = "123456"
        val inventory = createInventory()

        // when
        val result = inventory.getMovie(movieId)

        // then
        result shouldBe empty()
    }

    "should return empty result when movie does not exist" {
        // given
        val movieId = MovieId.generate().toString()
        val inventory = createInventory()

        // when
        val result = inventory.getMovie(movieId)

        // then
        result shouldBe empty()
    }

    "should return movie data when movie exists" {
        // given
        val inventory = createInventory()
        val title = "Test movie"
        val releaseDate = LocalDate.of(2020, Month.JUNE, 25)
        val movieId = inventory.addMovie(title, releaseDate)

        // when
        val result = inventory.getMovie(movieId)

        // then
        result shouldBeSome {
            it.title shouldBe title
            it.releaseDate shouldBe releaseDate
        }
    }
})