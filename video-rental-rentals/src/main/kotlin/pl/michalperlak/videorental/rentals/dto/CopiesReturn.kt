package pl.michalperlak.videorental.rentals.dto

import arrow.core.ListK
import pl.michalperlak.videorental.pricing.api.Price
import java.time.LocalDate

data class CopiesReturn(
    val returnId: String,
    val returnDate: LocalDate,
    val items: ListK<ReturnedCopy>
) {
    val totalDelayCharge: Price
        get() = items.fold(Price.ZERO) { acc, returnedCopy -> acc + returnedCopy.delayCharge }
}