package pl.michalperlak.videorental.pricing.api

import java.math.BigDecimal

data class Price internal constructor(
    private val amount: BigDecimal
) {
    operator fun plus(other: Price): Price =
        Price(amount + other.amount)

    operator fun times(factor: Long): Price =
        Price(amount * BigDecimal.valueOf(factor))

    fun format(): String = amount.toPlainString()

    companion object {
        val ZERO = of(0)

        fun of(value: Long): Price =
            Price(
                amount = BigDecimal.valueOf(value).setScale(2)
            )
    }
}
