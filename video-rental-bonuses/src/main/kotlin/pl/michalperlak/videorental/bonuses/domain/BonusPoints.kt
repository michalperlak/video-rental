package pl.michalperlak.videorental.bonuses.domain

internal data class BonusPoints(
    val customerId: String,
    val points: Int
)