package pl.michalperlak.videorental.rentals.domain

import java.util.UUID

internal data class ReturnId(
    private val id: UUID
) {
    override fun toString(): String = id.toString()

    companion object {
        fun generate(): ReturnId = ReturnId(UUID.randomUUID())
    }
}