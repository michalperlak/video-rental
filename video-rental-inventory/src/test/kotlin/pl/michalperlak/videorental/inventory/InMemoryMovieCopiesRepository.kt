package pl.michalperlak.videorental.inventory

import pl.michalperlak.videorental.inventory.domain.MovieCopiesRepository
import pl.michalperlak.videorental.inventory.domain.MovieCopy
import pl.michalperlak.videorental.inventory.domain.MovieCopyId
import java.util.concurrent.ConcurrentHashMap

class InMemoryMovieCopiesRepository : MovieCopiesRepository {
    private val values: MutableMap<MovieCopyId, MovieCopy> = ConcurrentHashMap()

    override fun addCopy(movieCopy: MovieCopy): MovieCopy =
        movieCopy.apply { values[id] = this }
}