package pl.michalperlak.videorental.inventory.mapper

import pl.michalperlak.videorental.inventory.domain.Rental
import pl.michalperlak.videorental.inventory.dto.RentedCopy
import pl.michalperlak.videorental.inventory.dto.Rental as RentalDto

internal fun Rental.asDto(): RentalDto =
    RentalDto(copies = copies.map {
        RentedCopy(copyId = it.id.toString(), movieId = it.movieId.toString())
    })