package pl.michalperlak.videorental.pricing.application

import pl.michalperlak.videorental.pricing.domain.MovieType
import pl.michalperlak.videorental.pricing.domain.Price
import pl.michalperlak.videorental.pricing.domain.Rental
import pl.michalperlak.videorental.pricing.domain.RentalItem
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
            MovieType.REGULAR_MOVIE -> baseRegularPrice * max(days - REGULAR_MOVIE_BONUS_DAYS,
                MIN_CHARGED_DAYS
            )
            MovieType.OLD_MOVIE -> baseRegularPrice * max(days - OLD_MOVIE_BONUS_DAYS,
                MIN_CHARGED_DAYS
            )
        }
    }

    companion object {
        private const val REGULAR_MOVIE_BONUS_DAYS = 2L
        private const val OLD_MOVIE_BONUS_DAYS = 4L
        private const val MIN_CHARGED_DAYS = 1L
    }
}