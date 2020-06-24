package pl.michalperlak.videorental.inventory

import pl.michalperlak.videorental.inventory.domain.Movie
import pl.michalperlak.videorental.inventory.domain.MoviesRepository
import java.util.concurrent.ConcurrentHashMap

class InMemoryMoviesRepository : MoviesRepository {
    private val movies: MutableMap<MovieId, Movie> = ConcurrentHashMap()

    override fun addMovie(movie: Movie): Movie =
        movie.apply { movies[id] = this }

    fun getAll(): List<Movie> = movies.values.toList()
}