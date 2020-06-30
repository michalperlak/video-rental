package pl.michalperlak.videorental.pricing.api

import java.time.Duration

data class RentalItem(
    val movieType: MovieType,
    val duration: Duration
)