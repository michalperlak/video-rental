package pl.michalperlak.videorental.pricing

import pl.michalperlak.videorental.pricing.api.DelayedReturn
import pl.michalperlak.videorental.pricing.api.Price
import pl.michalperlak.videorental.pricing.api.Rental
import pl.michalperlak.videorental.pricing.application.AdditionalChargesCalculator
import pl.michalperlak.videorental.pricing.application.PriceCalculator

internal class PricingFacade(
    private val priceCalculator: PriceCalculator,
    private val additionalChargesCalculator: AdditionalChargesCalculator
) : Prices {

    override fun computePrice(rental: Rental): Price = priceCalculator.computePrice(rental)

    override fun computeAdditionalCharges(delayedReturn: DelayedReturn): Price =
        additionalChargesCalculator.compute(delayedReturn)
}