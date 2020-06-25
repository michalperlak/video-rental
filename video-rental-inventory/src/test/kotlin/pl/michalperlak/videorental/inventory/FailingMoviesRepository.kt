package pl.michalperlak.videorental.inventory

import arrow.core.Option
import pl.michalperlak.videorental.inventory.domain.Movie
import pl.michalperlak.videorental.inventory.domain.MovieId
import pl.michalperlak.videorental.inventory.domain.MoviesRepository
import java.time.LocalDate

class FailingMoviesRepository(
    private val errorProducer: () -> Throwable
) : MoviesRepository {
    override fun addMovie(movie: Movie): Movie {
        throw errorProducer()
    }

    override fun findById(movieId: MovieId): Option<Movie> {
        throw errorProducer()
    }

    override fun findMovie(title: String, releaseDate: LocalDate): Option<Movie> {
        throw errorProducer()
    }
}