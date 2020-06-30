package pl.michalperlak.videorental.web.bonuses

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.michalperlak.videorental.bonuses.Bonuses
import pl.michalperlak.videorental.web.bonuses.BonusPointsController.Companion.BONUS_POINTS_PATH

@RestController
@RequestMapping(BONUS_POINTS_PATH)
class BonusPointsController(
    private val bonuses: Bonuses
) {
    @GetMapping("/{customerId}")
    fun getCustomerBonusPoints(@PathVariable customerId: String): BonusPointsDto =
        BonusPointsDto(customerId, bonuses.getCustomerPoints(customerId))

    companion object {
        const val BONUS_POINTS_PATH = "/api/bonus-points"
    }
}