package pl.michalperlak.videorental.rentals.domain

import arrow.core.Option

internal interface RentalsRepository {
    fun addRental(rental: Rental): Rental
    fun findById(rentalId: RentalId): Option<Rental>
}