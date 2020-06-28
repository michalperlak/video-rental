package pl.michalperlak.videorental.inventory

import arrow.core.extensions.option.align.empty
import io.kotest.assertions.arrow.option.shouldBeSome
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import pl.michalperlak.videorental.inventory.domain.MovieCopyId
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.Month
import java.time.ZoneOffset

class GetMovieCopySpec : StringSpec({

    "should return empty result when copy id is invalid" {
        // given
        val copyId = "123456"
        val inventory = createInventory()

        // when
        val result = inventory.getCopy(copyId)

        // then
        result shouldBe empty()
    }

    "should return empty result when copy does not exist" {
        // given
        val copyId = MovieCopyId.generate().toString()
        val inventory = createInventory()

        // when
        val result = inventory.getCopy(copyId)

        // then
        result shouldBe empty()
    }

    "should return movie copy data when it exists" {
        // given
        val currentTime = Instant.ofEpochSecond(234567891)
        val inventory = createInventory(clock = Clock.fixed(currentTime, ZoneOffset.UTC))
        val movieId = inventory.addMovie("Test movie", LocalDate.of(2020, Month.JUNE, 25))
        val copyId = inventory.addNewCopy(movieId)

        // when
        val result = inventory.getCopy(copyId)

        // then
        result shouldBeSome {
            it.movieId shouldBe movieId
            it.copyId shouldBe copyId
            it.added shouldBe currentTime
        }
    }

})