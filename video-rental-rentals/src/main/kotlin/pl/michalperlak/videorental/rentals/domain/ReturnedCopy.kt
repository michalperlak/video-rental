package pl.michalperlak.videorental.rentals.domain

import pl.michalperlak.videorental.pricing.api.Price
import java.time.Duration

internal data class ReturnedCopy(
    val copyId: String,
    val delay: Duration,
    val delayCharge: Price
)