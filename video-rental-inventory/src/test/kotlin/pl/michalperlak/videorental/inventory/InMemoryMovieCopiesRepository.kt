package pl.michalperlak.videorental.inventory

import pl.michalperlak.videorental.inventory.domain.MovieCopiesRepository
import pl.michalperlak.videorental.inventory.domain.MovieCopy
import pl.michalperlak.videorental.inventory.domain.MovieCopyId
import java.util.concurrent.ConcurrentHashMap

internal class InMemoryMovieCopiesRepository : MovieCopiesRepository {
    private val copies: MutableMap<MovieCopyId, MovieCopy> = ConcurrentHashMap()

    override fun addCopy(movieCopy: MovieCopy): MovieCopy =
        movieCopy.apply { copies[id] = this }

    fun getAll(): List<MovieCopy> = copies.values.toList()
}