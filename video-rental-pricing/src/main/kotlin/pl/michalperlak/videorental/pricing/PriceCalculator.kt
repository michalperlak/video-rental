package pl.michalperlak.videorental.pricing

import kotlin.math.max

class PriceCalculator(
    private val basePremiumPrice: Price,
    private val baseRegularPrice: Price
) {
    fun computePrice(rental: Rental): Price =
        rental
            .items
            .foldLeft(Price.ZERO) { acc, rentalItem -> acc + rentalItem.price() }

    private fun RentalItem.price(): Price {
        val days = duration.toDays()
        return when (movieType) {
            MovieType.NEW_RELEASE -> basePremiumPrice * days
            MovieType.REGULAR_MOVIE -> baseRegularPrice * max(days - 2, 1)
            MovieType.OLD_MOVIE -> baseRegularPrice * max(days - 4, 1)
        }
    }
}