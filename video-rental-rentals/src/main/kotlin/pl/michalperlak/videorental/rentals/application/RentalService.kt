package pl.michalperlak.videorental.rentals.application

import arrow.core.ListK
import pl.michalperlak.videorental.rentals.domain.Rental
import pl.michalperlak.videorental.rentals.domain.RentalId
import pl.michalperlak.videorental.rentals.domain.Return
import pl.michalperlak.videorental.rentals.dto.NewRental
import pl.michalperlak.videorental.inventory.dto.Rental as InventoryRental

internal interface RentalService {
    fun registerRental(rentalRequest: NewRental, inventoryRental: InventoryRental): Rental
    fun returnCopies(rentalId: RentalId, movieCopyIds: ListK<String>): Return
}