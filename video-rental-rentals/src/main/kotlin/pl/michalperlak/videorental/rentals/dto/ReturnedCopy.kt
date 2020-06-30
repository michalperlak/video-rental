package pl.michalperlak.videorental.rentals.dto

import pl.michalperlak.videorental.pricing.api.Price
import java.time.Duration

data class ReturnedCopy(
    val copyId: String,
    val delay: Duration,
    val delayCharge: Price
)