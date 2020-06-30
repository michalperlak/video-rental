package pl.michalperlak.videorental.rentals.mapper

import pl.michalperlak.videorental.rentals.domain.Rental
import pl.michalperlak.videorental.rentals.dto.RentedMovieCopy
import pl.michalperlak.videorental.rentals.dto.Rental as RentalDto

internal fun Rental.asDto(): RentalDto = RentalDto(
    rentalId = id.toString(),
    startDate = startDate,
    customerId = customerId,
    items = items.map { RentedMovieCopy(it.copyId, it.duration, it.price) })