package pl.michalperlak.videorental.web.rentals

import arrow.core.k
import pl.michalperlak.videorental.rentals.dto.NewRental
import pl.michalperlak.videorental.rentals.dto.NewRentalItem
import java.time.Duration

data class RentalDto(
    val customerId: String,
    val items: List<RentalItemDto>
)

data class RentalItemDto(
    val movieId: String,
    val days: Int
)

fun RentalDto.convert(): NewRental = NewRental(
    customerId = customerId,
    items = items.map { NewRentalItem(it.movieId, Duration.ofDays(it.days.toLong())) }.k()
)

