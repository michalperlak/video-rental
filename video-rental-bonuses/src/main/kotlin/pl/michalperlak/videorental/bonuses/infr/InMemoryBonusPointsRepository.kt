package pl.michalperlak.videorental.bonuses.infr

import arrow.core.Option
import pl.michalperlak.videorental.bonuses.domain.BonusPoints
import pl.michalperlak.videorental.bonuses.domain.BonusPointsRepository
import java.util.concurrent.ConcurrentHashMap

internal class InMemoryBonusPointsRepository : BonusPointsRepository {
    private val customerPoints: MutableMap<String, Int> = ConcurrentHashMap()

    @Synchronized
    override fun addPoints(bonus: BonusPoints): BonusPoints {
        val currentPoints = customerPoints[bonus.customerId] ?: 0
        customerPoints[bonus.customerId] = currentPoints + bonus.points
        return bonus
    }

    override fun getCustomerPoints(customerId: String): Option<BonusPoints> =
        Option
            .fromNullable(customerPoints[customerId])
            .map { BonusPoints(customerId, it) }
}