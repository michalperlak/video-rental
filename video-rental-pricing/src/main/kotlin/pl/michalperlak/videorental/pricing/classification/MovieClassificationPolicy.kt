package pl.michalperlak.videorental.pricing.classification

import pl.michalperlak.videorental.pricing.api.MovieType
import java.time.Clock
import java.time.LocalDate
import java.time.Period

interface MovieClassificationPolicy {
    fun classify(movieReleaseDate: LocalDate): MovieType

    companion object {
        fun default(newReleaseMaxAge: Period, oldMovieMinAge: Period, clock: Clock): MovieClassificationPolicy =
            SimpleMovieClassificationPolicy(newReleaseMaxAge, oldMovieMinAge, clock)
    }
}