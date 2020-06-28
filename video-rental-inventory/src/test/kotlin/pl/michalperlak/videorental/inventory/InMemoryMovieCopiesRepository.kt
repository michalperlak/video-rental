package pl.michalperlak.videorental.inventory

import arrow.core.Option
import arrow.core.SequenceK
import arrow.core.k
import pl.michalperlak.videorental.inventory.domain.MovieCopiesRepository
import pl.michalperlak.videorental.inventory.domain.MovieCopy
import pl.michalperlak.videorental.inventory.domain.MovieCopyId
import pl.michalperlak.videorental.inventory.domain.MovieCopyStatus
import pl.michalperlak.videorental.inventory.domain.MovieId
import java.util.concurrent.ConcurrentHashMap

internal class InMemoryMovieCopiesRepository : MovieCopiesRepository {
    private val copies: MutableMap<MovieCopyId, MovieCopy> = ConcurrentHashMap()

    override fun addCopy(movieCopy: MovieCopy): MovieCopy =
        movieCopy.apply { copies[id] = this }

    override fun findById(copyId: MovieCopyId): Option<MovieCopy> = Option.fromNullable(copies[copyId])

    override fun findByMovieAndStatus(movieId: MovieId, status: MovieCopyStatus): SequenceK<MovieCopy> =
        copies
            .values
            .asSequence()
            .filter { it.movieId == movieId && it.status == status }
            .k()

    fun getAll(): List<MovieCopy> = copies.values.toList()
}