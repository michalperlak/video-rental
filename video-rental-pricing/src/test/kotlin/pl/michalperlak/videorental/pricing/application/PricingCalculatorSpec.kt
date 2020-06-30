package pl.michalperlak.videorental.pricing.application

import arrow.core.ListK
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import pl.michalperlak.videorental.common.domain.MovieType
import pl.michalperlak.videorental.pricing.api.Rental
import pl.michalperlak.videorental.pricing.api.RentalItem
import java.time.Duration

class PricingCalculationsSpec : StringSpec({
    val calculator = PriceCalculator(
        basePremiumPrice = BASE_PREMIUM_PRICE,
        baseRegularPrice = BASE_REGULAR_PRICE
    )

    "for new releases price should be equal to premium rate times number of days" {
        // given
        val rental =
            newRelease(duration = Duration.ofDays(3))

        // when
        val price = calculator.computePrice(rental)

        // then
        price shouldBe BASE_PREMIUM_PRICE * 3
    }

    "for regular movies price for the first 3 days should be equal to the base" {
        forAll(
            row(Duration.ofDays(1)),
            row(Duration.ofDays(2)),
            row(Duration.ofDays(3))
        ) {
            // given
            val rental = regularMovie(it)

            // when
            val price = calculator.computePrice(rental)

            // then
            price shouldBe BASE_REGULAR_PRICE
        }
    }

    "for old movies price for the first 5 days should be equal to the base" {
        forAll(
            row(Duration.ofDays(1)),
            row(Duration.ofDays(2)),
            row(Duration.ofDays(3)),
            row(Duration.ofDays(4)),
            row(Duration.ofDays(5))
        ) {
            // given
            val rental = oldMovie(it)

            // when
            val price = calculator.computePrice(rental)

            // then
            price shouldBe BASE_REGULAR_PRICE
        }
    }

    "for regular movies price for each day after the 3rd should be equal to the base" {
        // given
        val rental =
            regularMovie(duration = Duration.ofDays(5))

        // when
        val price = calculator.computePrice(rental)

        // then
        price shouldBe BASE_REGULAR_PRICE + BASE_REGULAR_PRICE * 2
    }

    "for old movies price for each day after the 5th should be equal to the base" {
        // given
        val rental =
            oldMovie(duration = Duration.ofDays(7))

        // when
        val price = calculator.computePrice(rental)

        // then
        price shouldBe BASE_REGULAR_PRICE + BASE_REGULAR_PRICE * 2
    }

    "for mixed types of movies should return the sum of all prices" {
        // given
        val rental = newRelease(Duration.ofDays(2)) + regularMovie(
            Duration.ofDays(4)
        ) + oldMovie(Duration.ofDays(6))

        // when
        val price = calculator.computePrice(rental)

        // then
        price shouldBe (BASE_PREMIUM_PRICE * 2) + (BASE_REGULAR_PRICE + BASE_REGULAR_PRICE) + (BASE_REGULAR_PRICE + BASE_REGULAR_PRICE)
    }

})

private fun newRelease(duration: Duration): Rental =
    Rental(
        items = ListK.just(
            RentalItem(
                MovieType.NEW_RELEASE,
                duration
            )
        )
    )

private fun regularMovie(duration: Duration): Rental =
    Rental(
        items = ListK.just(
            RentalItem(
                MovieType.REGULAR_MOVIE,
                duration
            )
        )
    )

private fun oldMovie(duration: Duration): Rental =
    Rental(
        items = ListK.just(
            RentalItem(
                MovieType.OLD_MOVIE,
                duration
            )
        )
    )