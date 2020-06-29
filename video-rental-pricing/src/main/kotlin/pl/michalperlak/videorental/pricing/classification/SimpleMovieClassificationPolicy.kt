package pl.michalperlak.videorental.pricing.classification

import pl.michalperlak.videorental.pricing.api.MovieType
import java.time.Clock
import java.time.LocalDate
import java.time.Period

internal class SimpleMovieClassificationPolicy(
    private val newReleaseMaxAge: Period,
    private val oldMovieMinAge: Period,
    private val clock: Clock
) : MovieClassificationPolicy {

    override fun classify(movieReleaseDate: LocalDate): MovieType {
        val currentDate = LocalDate.now(clock)
        val newReleaseThreshold = currentDate.minus(newReleaseMaxAge)
        val oldMovieThreshold = currentDate.minus(oldMovieMinAge)
        return when {
            !movieReleaseDate.isBefore(newReleaseThreshold) -> MovieType.NEW_RELEASE
            !movieReleaseDate.isAfter(oldMovieThreshold) -> MovieType.OLD_MOVIE
            else -> MovieType.REGULAR_MOVIE
        }
    }
}