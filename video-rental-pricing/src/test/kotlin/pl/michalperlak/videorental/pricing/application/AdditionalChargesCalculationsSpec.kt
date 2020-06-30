package pl.michalperlak.videorental.pricing.application

import arrow.core.ListK
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import pl.michalperlak.videorental.common.domain.MovieType
import pl.michalperlak.videorental.pricing.api.DelayedReturn
import pl.michalperlak.videorental.pricing.api.LateReturnedItem
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

    "for old movies should charge regular base for each extra day" {
        // given
        val delayedReturn = oldMovieDelayedReturn(Duration.ofDays(5))

        // when
        val delayCharge = additionalChargesCalculator.compute(delayedReturn)

        // then
        delayCharge shouldBe BASE_REGULAR_DELAY_FEE * 5
    }

    "for mixed types of movies should charge sum of all delay charges" {
        // given
        val delayedReturn =
            newReleaseDelayedReturn(Duration.ofDays(1)) +
                    regularMovieDelayedReturn(Duration.ofDays(2)) +
                    oldMovieDelayedReturn(Duration.ofDays(3))

        // when
        val delayCharge = additionalChargesCalculator.compute(delayedReturn)

        // then
        delayCharge shouldBe (BASE_PREMIUM_DELAY_FEE) + (BASE_REGULAR_DELAY_FEE * 2) + (BASE_REGULAR_DELAY_FEE * 3)
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

private fun oldMovieDelayedReturn(delay: Duration): DelayedReturn =
    DelayedReturn(
        items = ListK.just(
            LateReturnedItem(MovieType.OLD_MOVIE, delay)
        )
    )