package pl.michalperlak.videorental.bonuses.domain

import arrow.core.Option

internal interface BonusPointsRepository {
    fun addPoints(bonus: BonusPoints): BonusPoints
    fun getCustomerPoints(customerId: String): Option<BonusPoints>
}