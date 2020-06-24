package pl.michalperlak.videorental.pricing.application

import pl.michalperlak.videorental.pricing.domain.DelayedReturn
import pl.michalperlak.videorental.pricing.domain.LateReturnedItem
import pl.michalperlak.videorental.pricing.domain.MovieType
import pl.michalperlak.videorental.pricing.domain.Price

class AdditionalChargesCalculator(
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
            MovieType.REGULAR_MOVIE -> regularDelayFeeBase * delayDays
            MovieType.NEW_RELEASE -> premiumDelayFeeBase * delayDays
            MovieType.OLD_MOVIE -> regularDelayFeeBase * delayDays
        }
    }
}