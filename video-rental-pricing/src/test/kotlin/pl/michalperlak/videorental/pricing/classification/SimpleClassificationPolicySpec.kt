package pl.michalperlak.videorental.pricing.classification

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import pl.michalperlak.videorental.pricing.api.MovieType
import java.time.Clock
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.Period
import java.time.ZoneOffset

class SimpleClassificationPolicySpec : StringSpec({
    val currentTime = LocalDateTime.of(2020, Month.JUNE, 29, 0, 0)
    val policy = SimpleMovieClassificationPolicy(
        newReleaseMaxAge = Period.ofDays(10),
        oldMovieMinAge = Period.ofDays(10 * 300),
        clock = Clock.fixed(
            currentTime.toInstant(ZoneOffset.UTC),
            ZoneOffset.UTC
        )
    )

    "should return new release when date is after new release threshold" {
        // given
        val movieReleaseDate = LocalDate.of(2020, Month.JUNE, 25)

        // when
        val movieType = policy.classify(movieReleaseDate)

        // then
        movieType shouldBe MovieType.NEW_RELEASE
    }

    "should return new release when date is equal to new release threshold" {
        // given
        val movieReleaseDate = LocalDate.of(2020, Month.JUNE, 19)

        // when
        val movieType = policy.classify(movieReleaseDate)

        // then
        movieType shouldBe MovieType.NEW_RELEASE
    }

    "should return old movie when date is before the old movie threshold" {
        // given
        val movieReleaseDate = LocalDate.of(2000, Month.JANUARY, 10)

        // when
        val movieType = policy.classify(movieReleaseDate)

        // then
        movieType shouldBe MovieType.OLD_MOVIE
    }

    "should return old movie when date is equal to old movie threshold" {
        // given
        val movieReleaseDate = currentTime.toLocalDate().minus(Period.ofDays(10 * 300))

        // when
        val movieType = policy.classify(movieReleaseDate)

        // then
        movieType shouldBe MovieType.OLD_MOVIE
    }

    "should return regular movie when date is between new release and old movie threshold values" {
        // given
        val movieReleaseDate = LocalDate.of(2016, Month.DECEMBER, 21)

        // when
        val movieType = policy.classify(movieReleaseDate)

        // then
        movieType shouldBe MovieType.REGULAR_MOVIE
    }
})