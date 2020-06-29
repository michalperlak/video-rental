package pl.michalperlak.videorental.rentals.domain

internal interface RentalsRepository {
    fun addRental(rental: Rental): Rental
}