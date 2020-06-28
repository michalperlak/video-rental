package pl.michalperlak.videorental.pricing

import pl.michalperlak.videorental.pricing.api.Price
import pl.michalperlak.videorental.pricing.api.Rental

interface Prices {
    fun computePrice(rental: Rental): Price
}