package pl.michalperlak.videorental.rentals

import arrow.core.Either
import pl.michalperlak.videorental.rentals.dto.NewRental
import pl.michalperlak.videorental.rentals.dto.Rental
import pl.michalperlak.videorental.rentals.error.RentalCreationError

interface Rentals {
    fun newRental(rental: NewRental): Either<RentalCreationError, Rental>
}