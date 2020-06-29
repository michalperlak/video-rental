package pl.michalperlak.videorental.rentals

import pl.michalperlak.videorental.inventory.Inventory

fun createRentals(inventory: Inventory): Rentals =
    RentalsFacade(inventory)