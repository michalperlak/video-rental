package pl.michalperlak.videorental.rentals.domain

import arrow.core.ListK
import java.time.LocalDate

internal data class Return(
    val id: ReturnId,
    val returnDate: LocalDate,
    val items: ListK<ReturnedCopy>
)
