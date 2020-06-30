package pl.michalperlak.videorental.rentals.domain

import pl.michalperlak.videorental.common.domain.MovieType
import pl.michalperlak.videorental.pricing.api.Price
import java.time.Duration

data class RentalItem(
    val copyId: String,
    val movieId: String,
    val movieType: MovieType,
    val duration: Duration,
    val price: Price
)