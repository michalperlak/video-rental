package pl.michalperlak.videorental.pricing.domain.domain

import arrow.core.ListK
import arrow.core.combineK
import java.time.Duration

data class RentalItem(
    val movieType: MovieType,
    val duration: Duration
)

data class Rental(
    val items: ListK<RentalItem>
) {
    operator fun plus(other: Rental): Rental = Rental(items.combineK(other.items))
}
