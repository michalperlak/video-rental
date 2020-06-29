package pl.michalperlak.videorental.rentals.dto

import java.time.Duration

data class NewRentalItem(
    val movieId: String,
    val duration: Duration,
    val copies: Int = DEFAULT_NUMBER_OF_COPIES
) {
    companion object {
        private const val DEFAULT_NUMBER_OF_COPIES = 1
    }
}