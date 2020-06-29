package pl.michalperlak.videorental.rentals.dto

import pl.michalperlak.videorental.pricing.api.Price
import java.time.Duration

data class RentedMovieCopy(
    val copyId: String,
    val duration: Duration,
    val price: Price
)