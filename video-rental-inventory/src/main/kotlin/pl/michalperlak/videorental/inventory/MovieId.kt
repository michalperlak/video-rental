package pl.michalperlak.videorental.inventory

import java.util.UUID

data class MovieId internal constructor(
        private val id: UUID
) {
    override fun toString(): String = id.toString()

    companion object {
        fun generate(): MovieId = MovieId(UUID.randomUUID())
    }
}