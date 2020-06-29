package pl.michalperlak.videorental.pricing

import arrow.core.k
import pl.michalperlak.videorental.pricing.api.DelayedReturn
import pl.michalperlak.videorental.pricing.api.LateReturnedItem
import pl.michalperlak.videorental.pricing.api.Price
import pl.michalperlak.videorental.pricing.api.Rental
import pl.michalperlak.videorental.pricing.api.RentalItem
import pl.michalperlak.videorental.pricing.application.AdditionalChargesCalculator
import pl.michalperlak.videorental.pricing.application.PriceCalculator

interface Prices {
    fun computePrice(rental: Rental): Price
    fun computeAdditionalCharges(delayedReturn: DelayedReturn): Price
    fun computePrice(rentalItem: RentalItem): Price = computePrice(Rental(items = listOf(rentalItem).k()))
    fun computeAdditionalCharges(lateReturnedItem: LateReturnedItem): Price =
        computeAdditionalCharges(DelayedReturn(items = listOf(lateReturnedItem).k()))

    companion object {
        fun createInstance(premiumBase: Price, regularBase: Price): Prices = PricingFacade(
            priceCalculator = PriceCalculator(
                baseRegularPrice = regularBase, basePremiumPrice = premiumBase),
            additionalChargesCalculator = AdditionalChargesCalculator(
                regularDelayFeeBase = regularBase, premiumDelayFeeBase = premiumBase)
        )
    }
}