package pl.michalperlak.videorental.rentals.dto

import java.time.Duration

data class NewRentalItem(
    val movieId: String,
    val duration: Duration
)