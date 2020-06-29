package pl.michalperlak.videorental.rentals.dto

import arrow.core.ListK
import pl.michalperlak.videorental.pricing.api.Price
import java.time.LocalDate

data class Rental(
    val startDate: LocalDate,
    val items: ListK<RentedMovieCopy>
) {
    val totalPrice: Price by lazy {
        items.map { it.price }.fold(Price.ZERO) { acc, price -> acc + price }
    }
}