package pl.michalperlak.videorental.rentals

import arrow.core.Either
import arrow.core.ListK
import pl.michalperlak.videorental.rentals.dto.CopiesReturn
import pl.michalperlak.videorental.rentals.dto.NewRental
import pl.michalperlak.videorental.rentals.dto.Rental
import pl.michalperlak.videorental.rentals.error.CopiesReturnError
import pl.michalperlak.videorental.rentals.error.RentalCreationError

interface Rentals {
    fun newRental(rental: NewRental): Either<RentalCreationError, Rental>
    fun returnCopies(rentalId: String, movieCopyIds: ListK<String>): Either<CopiesReturnError, CopiesReturn>
}