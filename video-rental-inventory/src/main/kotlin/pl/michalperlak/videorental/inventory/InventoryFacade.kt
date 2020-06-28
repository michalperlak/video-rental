package pl.michalperlak.videorental.inventory

import arrow.core.Either
import arrow.core.Option
import arrow.core.filterOrOther
import arrow.core.getOrElse
import pl.michalperlak.videorental.inventory.domain.MovieCopiesRepository
import pl.michalperlak.videorental.inventory.domain.MovieCopyId
import pl.michalperlak.videorental.inventory.domain.MovieId
import pl.michalperlak.videorental.inventory.domain.MoviesRepository
import pl.michalperlak.videorental.inventory.dto.Movie
import pl.michalperlak.videorental.inventory.dto.MovieCopy
import pl.michalperlak.videorental.inventory.dto.NewMovie
import pl.michalperlak.videorental.inventory.dto.NewMovieCopy
import pl.michalperlak.videorental.inventory.error.ErrorAddingMovie
import pl.michalperlak.videorental.inventory.error.ErrorAddingMovieCopy
import pl.michalperlak.videorental.inventory.error.InvalidMovieId
import pl.michalperlak.videorental.inventory.error.MovieAddingError
import pl.michalperlak.videorental.inventory.error.MovieAlreadyExists
import pl.michalperlak.videorental.inventory.error.MovieCopyAddingError
import pl.michalperlak.videorental.inventory.error.MovieIdNotFound
import pl.michalperlak.videorental.inventory.mapper.asDto
import pl.michalperlak.videorental.inventory.mapper.createMovie
import pl.michalperlak.videorental.inventory.mapper.createMovieCopy
import pl.michalperlak.videorental.inventory.util.execute
import java.time.Clock
import java.time.Instant

internal class InventoryFacade(
    private val moviesRepository: MoviesRepository,
    private val movieCopiesRepository: MovieCopiesRepository,
    private val clock: Clock
) : Inventory {
    override fun addMovie(newMovie: NewMovie): Either<MovieAddingError, Movie> =
        execute({
            moviesRepository
                .findMovie(newMovie.title, newMovie.releaseDate)
                .map { movieAlreadyRegistered(it.id) }
                .getOrElse { registerMovie(newMovie) }
        }, errorHandler = { ErrorAddingMovie(it) })

    override fun getMovie(movieId: String): Option<Movie> =
        MovieId
            .from(movieId)
            .flatMap { moviesRepository.findById(it) }
            .map { it.asDto() }

    override fun addNewCopy(newMovieCopy: NewMovieCopy): Either<MovieCopyAddingError, MovieCopy> =
        execute({
            newMovieCopy
                .createMovieCopy(MovieCopyId.generate(), Instant.now(clock))
                .toEither { InvalidMovieId(newMovieCopy.movieId) }
                .filterOrOther(predicate = { movieExists(it.movieId) }) {
                    MovieIdNotFound(it.movieId.toString())
                }
                .map { movieCopiesRepository.addCopy(it) }
                .map { it.asDto() }
        }, errorHandler = { ErrorAddingMovieCopy(it) })

    override fun getCopy(copyId: String): Option<MovieCopy> =
        MovieCopyId
            .from(copyId)
            .flatMap { movieCopiesRepository.findById(it) }
            .map { it.asDto() }

    private fun registerMovie(newMovie: NewMovie): Either<MovieAddingError, Movie> =
        Either.right(
            moviesRepository
                .addMovie(newMovie.createMovie(movieId = MovieId.generate()))
                .asDto()
        )

    private fun movieAlreadyRegistered(movieId: MovieId): Either<MovieAddingError, Movie> =
        Either.left(MovieAlreadyExists(movieId.toString()))

    private fun movieExists(movieId: MovieId): Boolean =
        moviesRepository
            .findById(movieId)
            .isDefined()
}
