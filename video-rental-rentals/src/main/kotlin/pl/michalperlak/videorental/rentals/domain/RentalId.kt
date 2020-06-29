package pl.michalperlak.videorental.rentals.domain

import java.util.UUID

internal data class RentalId(
    private val id: UUID
) {
    override fun toString(): String = id.toString()

    companion object {
        fun generate(): RentalId = RentalId(UUID.randomUUID())
    }
}