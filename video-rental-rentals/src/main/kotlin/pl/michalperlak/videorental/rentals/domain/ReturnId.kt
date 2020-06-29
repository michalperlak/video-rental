package pl.michalperlak.videorental.rentals.domain

import java.util.UUID

internal data class ReturnId(
    private val id: UUID
) {
    companion object {
        fun generate(): ReturnId = ReturnId(UUID.randomUUID())
    }
}