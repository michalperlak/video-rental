package pl.michalperlak.videorental.inventory.domain

import arrow.core.Option
import arrow.core.nonFatalOrThrow
import java.util.UUID

internal data class MovieCopyId internal constructor(
    private val id: UUID
) {
    override fun toString(): String = id.toString()

    fun asUUID(): UUID = id

    companion object {
        fun generate(): MovieCopyId =
            MovieCopyId(UUID.randomUUID())

        fun from(value: String): Option<MovieCopyId> = try {
            val id = UUID.fromString(value)
            Option.just(MovieCopyId(id))
        } catch (e: Exception) {
            e.nonFatalOrThrow()
            Option.empty()
        }
    }
}