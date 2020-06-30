package pl.michalperlak.videorental.pricing.api

import pl.michalperlak.videorental.common.domain.MovieType
import java.time.Duration

data class LateReturnedItem(
    val movieType: MovieType,
    val delay: Duration
)