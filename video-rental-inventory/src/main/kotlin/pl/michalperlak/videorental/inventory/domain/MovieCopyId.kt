package pl.michalperlak.videorental.inventory.domain

import java.util.UUID

data class MovieCopyId internal constructor(
    private val id: UUID
) {
    override fun toString(): String = id.toString()

    companion object {
        fun generate(): MovieCopyId =
            MovieCopyId(UUID.randomUUID())
    }
}