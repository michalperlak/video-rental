package pl.michalperlak.videorental.rentals.domain

import arrow.core.Option
import arrow.core.nonFatalOrThrow
import java.util.UUID

internal data class RentalId(
    private val id: UUID
) {
    override fun toString(): String = id.toString()

    companion object {
        fun generate(): RentalId = RentalId(UUID.randomUUID())

        fun from(value: String): Option<RentalId> = try {
            Option.just(RentalId(id = UUID.fromString(value)))
        } catch (e: Exception) {
            e.nonFatalOrThrow()
            Option.empty()
        }
    }
}