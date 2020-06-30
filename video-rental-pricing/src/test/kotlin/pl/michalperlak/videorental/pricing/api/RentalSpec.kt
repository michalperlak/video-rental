package pl.michalperlak.videorental.pricing.api

import arrow.core.k
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldHaveSize
import java.time.Duration

class RentalSpec : StringSpec({

    "result of the rentals sum should contain all the items" {
        // given
        val rental1 = Rental(items = listOf(NEW_RELEASE_RENTAL).k())
        val rental2 = Rental(items = listOf(REGULAR_MOVIE_RENTAL).k())
        val rental3 = Rental(items = listOf(OLD_MOVIE_RENTAL).k())

        // when
        val sum = rental1 + rental2 + rental3

        // then
        sum.items shouldHaveSize 3
        sum.items shouldContainAll listOf(NEW_RELEASE_RENTAL, REGULAR_MOVIE_RENTAL, OLD_MOVIE_RENTAL)
    }

})

internal val NEW_RELEASE_RENTAL = RentalItem(
    MovieType.NEW_RELEASE,
    Duration.ofDays(1)
)
internal val REGULAR_MOVIE_RENTAL = RentalItem(
    MovieType.REGULAR_MOVIE,
    Duration.ofDays(1)
)
internal val OLD_MOVIE_RENTAL = RentalItem(
    MovieType.OLD_MOVIE,
    Duration.ofDays(1)
)