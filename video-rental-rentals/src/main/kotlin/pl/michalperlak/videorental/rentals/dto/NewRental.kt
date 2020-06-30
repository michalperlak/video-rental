package pl.michalperlak.videorental.rentals.dto

import arrow.core.ListK

data class NewRental(
    val customerId: String,
    val items: ListK<NewRentalItem>
)