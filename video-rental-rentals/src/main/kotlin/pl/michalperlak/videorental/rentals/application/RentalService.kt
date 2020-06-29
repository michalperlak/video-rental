package pl.michalperlak.videorental.rentals.application

import pl.michalperlak.videorental.rentals.domain.Rental
import pl.michalperlak.videorental.rentals.dto.NewRental
import pl.michalperlak.videorental.inventory.dto.Rental as InventoryRental

internal interface RentalService {
    fun registerRental(rentalRequest: NewRental, inventoryRental: InventoryRental): Rental
}