package pl.michalperlak.videorental.bonuses

import arrow.core.getOrElse
import pl.michalperlak.videorental.bonuses.domain.BonusPointsRepository

internal class BonusesFacade(
    private val bonusPointsRepository: BonusPointsRepository
) : Bonuses {

    override fun getCustomerPoints(customerId: String): Int =
        bonusPointsRepository
            .getCustomerPoints(customerId)
            .map { it.points }
            .getOrElse { 0 }
}