package pl.michalperlak.videorental.rentals.dto

import arrow.core.ListK

data class NewRental(
    val items: ListK<NewRentalItem>
)