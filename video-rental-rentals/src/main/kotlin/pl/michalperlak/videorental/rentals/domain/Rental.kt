package pl.michalperlak.videorental.rentals.domain

import arrow.core.ListK
import java.time.LocalDate

internal data class Rental(
    val id: RentalId,
    val customerId: String,
    val startDate: LocalDate,
    val items: ListK<RentalItem>
)