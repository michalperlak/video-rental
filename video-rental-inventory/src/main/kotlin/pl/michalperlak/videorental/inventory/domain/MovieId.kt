package pl.michalperlak.videorental.inventory.domain

import arrow.core.Option
import arrow.core.nonFatalOrThrow
import java.util.UUID

internal data class MovieId internal constructor(
    private val id: UUID
) {
    override fun toString(): String = id.toString()

    companion object {
        fun generate(): MovieId =
            MovieId(UUID.randomUUID())

        fun from(value: String): Option<MovieId> = try {
            val id = UUID.fromString(value)
            Option.just(MovieId(id))
        } catch (e: Exception) {
            e.nonFatalOrThrow()
            Option.empty()
        }
    }
}