package pl.michalperlak.videorental.inventory

import arrow.core.Option
import pl.michalperlak.videorental.inventory.domain.Movie
import pl.michalperlak.videorental.inventory.domain.MovieId
import pl.michalperlak.videorental.inventory.domain.MoviesRepository
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap

internal class InMemoryMoviesRepository : MoviesRepository {
    private val movies: MutableMap<MovieId, Movie> = ConcurrentHashMap()

    override fun addMovie(movie: Movie): Movie =
        movie.apply { movies[id] = this }

    override fun findById(movieId: MovieId): Option<Movie> = Option.fromNullable(movies[movieId])

    override fun findMovie(title: String, releaseDate: LocalDate): Option<Movie> =
        Option.fromNullable(
            movies.values.firstOrNull { it.title == title && it.releaseDate == releaseDate }
        )

    fun getAll(): List<Movie> = movies.values.toList()
}