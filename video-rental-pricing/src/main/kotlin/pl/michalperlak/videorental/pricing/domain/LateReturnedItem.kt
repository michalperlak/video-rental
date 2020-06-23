package pl.michalperlak.videorental.pricing.domain

import java.time.Duration

data class LateReturnedItem(
    val movieType: MovieType,
    val delay: Duration
)