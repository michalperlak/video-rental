package pl.michalperlak.videorental.inventory

import arrow.core.Either
import arrow.core.ListK
import arrow.core.Option
import arrow.core.filterOrOther
import arrow.core.getOrElse
import pl.michalperlak.videorental.inventory.application.MovieRentalService
import pl.michalperlak.videorental.inventory.domain.MovieCopiesRepository
import pl.michalperlak.videorental.inventory.domain.MovieCopyId
import pl.michalperlak.videorental.inventory.domain.MovieId
import pl.michalperlak.videorental.inventory.domain.MoviesRepository
import pl.michalperlak.videorental.inventory.dto.Movie
import pl.michalperlak.videorental.inventory.dto.MovieCopy
import pl.michalperlak.videorental.inventory.dto.MovieToRent
import pl.michalperlak.videorental.inventory.dto.NewMovie
import pl.michalperlak.videorental.inventory.dto.NewMovieCopy
import pl.michalperlak.videorental.inventory.dto.Rental
import pl.michalperlak.videorental.inventory.error.*
import pl.michalperlak.videorental.inventory.mapper.asDto
import pl.michalperlak.videorental.inventory.mapper.createMovie
import pl.michalperlak.videorental.inventory.mapper.createMovieCopy
import pl.michalperlak.videorental.inventory.mapper.toRentalItem
import pl.michalperlak.videorental.inventory.util.executeAndHandle
import pl.michalperlak.videorental.inventory.util.executeForEither
import pl.michalperlak.videorental.inventory.util.flatMapOption
import java.time.Clock
import java.time.Instant

internal class InventoryFacade(
    private val moviesRepository: MoviesRepository,
    private val movieCopiesRepository: MovieCopiesRepository,
    private val movieRentalService: MovieRentalService,
    private val clock: Clock
) : Inventory {
    override fun addMovie(newMovie: NewMovie): Either<MovieAddingError, Movie> =
        executeForEither({
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
        executeForEither({
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

    override fun rentMovies(movies: ListK<MovieToRent>): Either<RentalInventoryError, Rental> =
        executeAndHandle({
            movieRentalService
                .rent(movies.flatMapOption(MovieToRent::toRentalItem))
                .asDto()
        }, errorHandler = {
            when (it) {
                is CopiesNotAvailableException -> CopiesNotAvailable(it.movieId)
                else -> ErrorRentingCopies(it)
            }
        })

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
