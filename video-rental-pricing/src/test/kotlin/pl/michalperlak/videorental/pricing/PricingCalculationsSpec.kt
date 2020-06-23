package pl.michalperlak.videorental.pricing

import arrow.core.ListK
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import java.time.Duration

val BASE_PREMIUM_PRICE = Price.of(40)
val BASE_REGULAR_PRICE = Price.of(30)

class PricingCalculationsSpec : StringSpec({
    val calculator = PriceCalculator(basePremiumPrice = BASE_PREMIUM_PRICE, baseRegularPrice = BASE_REGULAR_PRICE)

    "for new releases price should be equal to premium rate times number of days" {
        // given
        val rental = newRelease(duration = Duration.ofDays(3))

        // when
        val price = calculator.computePrice(rental)

        // then
        price shouldBe Price.of(120)
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
            price shouldBe Price.of(30)
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
            price shouldBe Price.of(30)
        }
    }

    "for regular movies price for each day after the 3rd should be equal to the base" {
        // given
        val rental = regularMovie(duration = Duration.ofDays(5))

        // when
        val price = calculator.computePrice(rental)

        // then
        price shouldBe Price.of(90)
    }

    "for old movies price for each day after the 5th should be equal to the base" {
        // given
        val rental = oldMovie(duration = Duration.ofDays(7))

        // when
        val price = calculator.computePrice(rental)

        // then
        price shouldBe Price.of(90)
    }

})

private fun newRelease(duration: Duration): Rental =
    Rental(items = ListK.just(RentalItem(MovieType.NEW_RELEASE, duration)))

private fun regularMovie(duration: Duration): Rental =
    Rental(items = ListK.just(RentalItem(MovieType.REGULAR_MOVIE, duration)))

private fun oldMovie(duration: Duration): Rental =
    Rental(items = ListK.just(RentalItem(MovieType.OLD_MOVIE, duration)))