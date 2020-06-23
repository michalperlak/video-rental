package pl.michalperlak.videorental.pricing.domain

import java.time.Duration

data class RentalItem(
    val movieType: MovieType,
    val duration: Duration
)