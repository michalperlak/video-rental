package pl.michalperlak.videorental.pricing

import kotlin.math.min

class PriceCalculator {
    fun computePrice(rental: Rental): Price =
        rental
            .items
            .foldLeft(Price.ZERO) { acc, rentalItem -> acc + rentalItem.price() }

    private fun RentalItem.price(): Price {
        val days = duration.toDays()
        return when (movieType) {
            MovieType.NEW_RELEASE -> BASE_PREMIUM_PRICE * days
            MovieType.REGULAR_MOVIE -> BASE_REGULAR_PRICE * min(days - 2, 1)
            MovieType.OLD_MOVIE -> BASE_REGULAR_PRICE * min(days - 4, 1)
        }
    }

    companion object {
        private val BASE_PREMIUM_PRICE = Price.of(40)
        private val BASE_REGULAR_PRICE = Price.of(30)
    }
}