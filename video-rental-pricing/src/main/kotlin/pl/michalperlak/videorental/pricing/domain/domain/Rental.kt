package pl.michalperlak.videorental.pricing.domain.domain

import arrow.core.ListK
import arrow.core.combineK

data class Rental(
    val items: ListK<RentalItem>
) {
    operator fun plus(other: Rental): Rental = Rental(items.combineK(other.items))
}
