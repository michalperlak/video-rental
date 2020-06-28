package pl.michalperlak.videorental.pricing.domain

import arrow.core.ListK
import arrow.core.combineK

internal data class Rental(
    val items: ListK<RentalItem>
) {
    operator fun plus(other: Rental): Rental =
        Rental(items.combineK(other.items))
}
