package pl.michalperlak.videorental.pricing

import arrow.core.ListK
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Duration

class PricingCalculationsSpec : StringSpec({
    val calculator = PriceCalculator()

    "for new releases price should be equal to premium rate times number of days" {
        // given
        val rental = newRelease(duration = Duration.ofDays(3))

        // when
        val price = calculator.computePrice(rental)

        // then
        price shouldBe Price.of(120)
    }

    "for regular movies price for the first 3 days should be equal to the base" {
        // given
        val rental = regularMovie(duration = Duration.ofDays(3))

        // when
        val price = calculator.computePrice(rental)

        // then
        price shouldBe Price.of(30)
    }

    "for old movies price for the first 5 days should be equal to the base" {
        // given
        val rental = oldMovie(duration = Duration.ofDays(5))

        // when
        val price = calculator.computePrice(rental)

        // then
        price shouldBe Price.of(30)
    }

})

private fun newRelease(duration: Duration): Rental =
    Rental(items = ListK.just(RentalItem(MovieType.NEW_RELEASE, duration)))

private fun regularMovie(duration: Duration): Rental =
    Rental(items = ListK.just(RentalItem(MovieType.REGULAR_MOVIE, duration)))

private fun oldMovie(duration: Duration): Rental =
    Rental(items = ListK.just(RentalItem(MovieType.OLD_MOVIE, duration)))