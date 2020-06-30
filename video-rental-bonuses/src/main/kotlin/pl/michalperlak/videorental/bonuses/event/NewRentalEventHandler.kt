package pl.michalperlak.videorental.bonuses.event

import pl.michalperlak.videorental.bonuses.domain.BonusPoints
import pl.michalperlak.videorental.bonuses.domain.BonusPointsRepository
import pl.michalperlak.videorental.common.domain.MovieType
import pl.michalperlak.videorental.common.events.Handler
import pl.michalperlak.videorental.common.events.rentals.NewRentalCreated

internal class NewRentalEventHandler(
    private val bonusPointsRepository: BonusPointsRepository
) : Handler<NewRentalCreated> {

    override fun invoke(event: NewRentalCreated) {
        val points = event.items.fold(0) { acc, movieType -> acc + bonusPoints(movieType) }
        bonusPointsRepository.addPoints(
            BonusPoints(event.customerId, points)
        )
    }

    private fun bonusPoints(movieType: MovieType): Int = when (movieType) {
        MovieType.NEW_RELEASE -> NEW_RELEASE_RENTAL_BONUS
        else -> REGULAR_RENTAL_BONUS
    }

    companion object {
        private const val NEW_RELEASE_RENTAL_BONUS = 2
        private const val REGULAR_RENTAL_BONUS = 1
    }
}