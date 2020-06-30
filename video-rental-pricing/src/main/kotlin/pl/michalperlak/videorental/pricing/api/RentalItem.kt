package pl.michalperlak.videorental.pricing.api

import pl.michalperlak.videorental.common.domain.MovieType
import java.time.Duration

data class RentalItem(
    val movieType: MovieType,
    val duration: Duration
)