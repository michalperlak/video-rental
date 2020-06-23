package pl.michalperlak.videorental.pricing

class PriceCalculator {
    fun computePrice(rental: Rental): Price =
        rental
            .items
            .foldLeft(Price.ZERO) { acc, rentalItem -> acc + rentalItem.price() }

    private fun RentalItem.price(): Price {
        val days = duration.toDays()
        val base = when (movieType) {
            MovieType.NEW_RELEASE -> BASE_PREMIUM_PRICE
            else -> BASE_REGULAR_PRICE
        }
        return base * days
    }

    companion object {
        private val BASE_PREMIUM_PRICE = Price.of(40)
        private val BASE_REGULAR_PRICE = Price.of(30)
    }
}