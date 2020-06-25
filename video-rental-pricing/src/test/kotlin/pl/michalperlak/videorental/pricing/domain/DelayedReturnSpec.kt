package pl.michalperlak.videorental.pricing.domain

import arrow.core.k
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldHaveSize
import java.time.Duration

class DelayedReturnSpec : StringSpec({

    "result of the delayed returns sum should contain all items" {
        // given
        val newRelease = DelayedReturn(items = listOf(NEW_RELEASE_RETURNED_LATE).k())
        val regularMovie = DelayedReturn(items = listOf(REGULAR_MOVIE_RETURNED_LATE).k())
        val oldMovie = DelayedReturn(items = listOf(OLD_MOVIE_RETURNED_LATE).k())

        // when
        val sum = newRelease + regularMovie + oldMovie

        // then
        sum.items shouldHaveSize 3
        sum.items shouldContainAll listOf(
            NEW_RELEASE_RETURNED_LATE,
            REGULAR_MOVIE_RETURNED_LATE,
            OLD_MOVIE_RETURNED_LATE
        )
    }
})

internal val NEW_RELEASE_RETURNED_LATE = LateReturnedItem(
    MovieType.NEW_RELEASE,
    Duration.ofDays(1)
)
internal val REGULAR_MOVIE_RETURNED_LATE = LateReturnedItem(
    MovieType.REGULAR_MOVIE,
    Duration.ofDays(2)
)
internal val OLD_MOVIE_RETURNED_LATE = LateReturnedItem(
    MovieType.OLD_MOVIE,
    Duration.ofDays(3)
)