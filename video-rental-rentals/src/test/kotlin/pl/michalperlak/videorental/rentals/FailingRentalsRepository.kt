package pl.michalperlak.videorental.rentals

import arrow.core.Option
import pl.michalperlak.videorental.rentals.domain.Rental
import pl.michalperlak.videorental.rentals.domain.RentalId
import pl.michalperlak.videorental.rentals.domain.RentalsRepository

internal class FailingRentalsRepository(
    private val errorProducer: () -> Throwable
) : RentalsRepository {

    override fun addRental(rental: Rental): Rental {
        throw errorProducer()
    }

    override fun findById(rentalId: RentalId): Option<Rental> {
        throw errorProducer()
    }
}