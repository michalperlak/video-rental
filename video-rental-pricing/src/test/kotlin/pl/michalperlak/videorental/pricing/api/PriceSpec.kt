package pl.michalperlak.videorental.pricing.api

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PriceSpec : StringSpec({

    "should sum prices correctly" {
        // given
        val price1 = Price.of(10)
        val price2 = Price.of(20)

        // when
        val sum = price1 + price2

        // then
        sum shouldBe Price.of(30)
    }

    "should multiply price correctly" {
        // given
        val factor = 5L
        val price = Price.of(10)

        // when
        val result = price * factor

        // then
        result shouldBe Price.of(50)
    }
})