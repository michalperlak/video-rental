package pl.michalperlak.videorental.pricing.api

import java.time.Duration

data class LateReturnedItem(
    val movieType: MovieType,
    val delay: Duration
)