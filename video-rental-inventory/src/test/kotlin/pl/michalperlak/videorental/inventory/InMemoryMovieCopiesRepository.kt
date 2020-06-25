package pl.michalperlak.videorental.inventory

import arrow.core.Option
import pl.michalperlak.videorental.inventory.domain.MovieCopiesRepository
import pl.michalperlak.videorental.inventory.domain.MovieCopy
import pl.michalperlak.videorental.inventory.domain.MovieCopyId
import java.util.concurrent.ConcurrentHashMap

internal class InMemoryMovieCopiesRepository : MovieCopiesRepository {
    private val copies: MutableMap<MovieCopyId, MovieCopy> = ConcurrentHashMap()

    override fun addCopy(movieCopy: MovieCopy): MovieCopy =
        movieCopy.apply { copies[id] = this }

    override fun findById(copyId: MovieCopyId): Option<MovieCopy> = Option.fromNullable(copies[copyId])

    fun getAll(): List<MovieCopy> = copies.values.toList()
}