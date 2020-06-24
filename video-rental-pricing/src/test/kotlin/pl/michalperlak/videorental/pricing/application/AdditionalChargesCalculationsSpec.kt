package pl.michalperlak.videorental.pricing.application

import arrow.core.ListK
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import pl.michalperlak.videorental.pricing.domain.DelayedReturn
import pl.michalperlak.videorental.pricing.domain.LateReturnedItem
import pl.michalperlak.videorental.pricing.domain.MovieType
import pl.michalperlak.videorental.pricing.domain.Price
import java.time.Duration

class AdditionalChargesCalculationsSpec : StringSpec({
    val additionalChargesCalculator = AdditionalChargesCalculator(
        regularDelayFeeBase = BASE_REGULAR_DELAY_FEE, premiumDelayFeeBase = BASE_PREMIUM_DELAY_FEE
    )

    "for regular movies should charge regular base for each extra day" {
        // given
        val delayedReturn = regularMovieDelayedReturn(Duration.ofDays(2))

        // when
        val delayCharge = additionalChargesCalculator.compute(delayedReturn)

        // then
        delayCharge shouldBe BASE_REGULAR_DELAY_FEE * 2
    }

    "for new releases should charge premium base for each extra day" {
        // given
        val delayedReturn = newReleaseDelayedReturn(Duration.ofDays(3))

        // when
        val delayCharge = additionalChargesCalculator.compute(delayedReturn)

        // then
        delayCharge shouldBe BASE_PREMIUM_DELAY_FEE * 3
    }

})

private fun regularMovieDelayedReturn(delay: Duration): DelayedReturn =
    DelayedReturn(
        items = ListK.just(
            LateReturnedItem(MovieType.REGULAR_MOVIE, delay)
        )
    )

private fun newReleaseDelayedReturn(delay: Duration): DelayedReturn =
    DelayedReturn(
        items = ListK.just(
            LateReturnedItem(MovieType.NEW_RELEASE, delay)
        )
    )