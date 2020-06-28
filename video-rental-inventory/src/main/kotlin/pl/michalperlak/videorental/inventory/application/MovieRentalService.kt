package pl.michalperlak.videorental.inventory.application

import arrow.core.ListK
import pl.michalperlak.videorental.inventory.domain.Rental
import pl.michalperlak.videorental.inventory.domain.RentalItem

internal interface MovieRentalService {
    fun rent(rentalItems: ListK<RentalItem>): Rental
}