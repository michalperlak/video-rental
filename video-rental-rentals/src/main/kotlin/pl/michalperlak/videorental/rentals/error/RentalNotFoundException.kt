package pl.michalperlak.videorental.rentals.error

import pl.michalperlak.videorental.rentals.domain.RentalId

internal class RentalNotFoundException(val rentalId: RentalId) :
    RuntimeException("Rental with id: $rentalId not found")