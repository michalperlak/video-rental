package pl.michalperlak.videorental.pricing

import arrow.core.ListK
import java.time.Duration

data class RentalItem(
    val movieType: MovieType,
    val duration: Duration
)

data class Rental(
    val items: ListK<RentalItem>
)
