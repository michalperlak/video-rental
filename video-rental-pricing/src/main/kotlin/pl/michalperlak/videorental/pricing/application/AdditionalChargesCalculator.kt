package pl.michalperlak.videorental.pricing.application

import pl.michalperlak.videorental.pricing.api.DelayedReturn
import pl.michalperlak.videorental.pricing.api.LateReturnedItem
import pl.michalperlak.videorental.pricing.api.MovieType
import pl.michalperlak.videorental.pricing.api.Price

internal class AdditionalChargesCalculator(
    private val regularDelayFeeBase: Price,
    private val premiumDelayFeeBase: Price
) {
    fun compute(delayedReturn: DelayedReturn): Price =
        delayedReturn
            .items
            .foldLeft(Price.ZERO) { acc, lateReturnedItem -> acc + lateReturnedItem.delayFee() }

    private fun LateReturnedItem.delayFee(): Price {
        val delayDays = delay.toDays()
        return when (movieType) {
            MovieType.NEW_RELEASE -> premiumDelayFeeBase * delayDays
            MovieType.REGULAR_MOVIE, MovieType.OLD_MOVIE -> regularDelayFeeBase * delayDays
        }
    }
}