package pl.michalperlak.videorental.inventory.dto

import arrow.core.ListK

data class Rental(
    val copies: ListK<RentedCopy>
)