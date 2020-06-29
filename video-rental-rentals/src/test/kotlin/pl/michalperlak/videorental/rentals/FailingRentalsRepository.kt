package pl.michalperlak.videorental.rentals

import pl.michalperlak.videorental.rentals.domain.Rental
import pl.michalperlak.videorental.rentals.domain.RentalsRepository

internal class FailingRentalsRepository(
    private val errorProducer: () -> Throwable
) : RentalsRepository {

    override fun addRental(rental: Rental): Rental {
        throw errorProducer()
    }
}