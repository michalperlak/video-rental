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

})

private fun newRelease(duration: Duration): Rental =
    Rental(items = ListK.just(RentalItem(MovieType.NEW_RELEASE, duration)))