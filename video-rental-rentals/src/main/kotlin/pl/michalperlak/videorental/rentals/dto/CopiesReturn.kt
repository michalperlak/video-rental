package pl.michalperlak.videorental.rentals.dto

import arrow.core.ListK
import java.time.LocalDate

data class CopiesReturn(
    val returnId: String,
    val returnDate: LocalDate,
    val items: ListK<ReturnedCopy>
)