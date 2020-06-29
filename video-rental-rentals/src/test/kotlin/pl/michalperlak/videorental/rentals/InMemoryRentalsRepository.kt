package pl.michalperlak.videorental.rentals

import arrow.core.Option
import pl.michalperlak.videorental.rentals.domain.Rental
import pl.michalperlak.videorental.rentals.domain.RentalId
import pl.michalperlak.videorental.rentals.domain.RentalsRepository
import java.util.concurrent.ConcurrentHashMap

internal class InMemoryRentalsRepository : RentalsRepository {
    private val rentals: MutableMap<RentalId, Rental> = ConcurrentHashMap()

    override fun addRental(rental: Rental): Rental = rental.apply { rentals[id] = this }

    override fun findById(rentalId: RentalId): Option<Rental> = Option.fromNullable(rentals[rentalId])

    fun getAll(): List<Rental> = rentals.values.toList()
}