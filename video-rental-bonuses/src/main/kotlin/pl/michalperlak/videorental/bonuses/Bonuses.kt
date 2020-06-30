package pl.michalperlak.videorental.bonuses

import pl.michalperlak.videorental.bonuses.event.NewRentalEventHandler
import pl.michalperlak.videorental.bonuses.infr.InMemoryBonusPointsRepository
import pl.michalperlak.videorental.common.events.Events
import pl.michalperlak.videorental.common.events.subscribe

interface Bonuses {
    fun getCustomerPoints(customerId: String): Int

    companion object {
        fun configure(events: Events): Bonuses {
            val bonusPointsRepository = InMemoryBonusPointsRepository()
            events.subscribe(NewRentalEventHandler(bonusPointsRepository))
            return BonusesFacade(bonusPointsRepository)
        }
    }
}