package pl.michalperlak.videorental.inventory

import arrow.core.Either
import arrow.core.getOrElse
import pl.michalperlak.videorental.inventory.domain.MoviesRepository
import pl.michalperlak.videorental.inventory.dto.NewMovie
import pl.michalperlak.videorental.inventory.error.MovieAddingError
import pl.michalperlak.videorental.inventory.error.MovieAlreadyExists
import pl.michalperlak.videorental.inventory.mapper.asDto
import pl.michalperlak.videorental.inventory.mapper.createMovie
import pl.michalperlak.videorental.inventory.util.of
import pl.michalperlak.videorental.inventory.dto.Movie as MovieDto

class InventoryFacade(
    private val moviesRepository: MoviesRepository
) {
    fun addMovie(newMovie: NewMovie): Either<MovieAddingError, MovieDto> =
        moviesRepository
            .findMovie(newMovie.title, newMovie.releaseDate)
            .map { movieAlreadyRegistered(it.id) }
            .getOrElse { registerMovie(newMovie) }

    private fun registerMovie(newMovie: NewMovie): Either<MovieAddingError, MovieDto> =
        Either
            .of {
                val movie = newMovie.createMovie(movieId = MovieId.generate())
                moviesRepository.addMovie(movie)
            }
            .map { it.asDto() }
            .mapLeft { TODO() }

    private fun movieAlreadyRegistered(movieId: MovieId): Either<MovieAddingError, MovieDto> =
        Either.left(MovieAlreadyExists(movieId.toString()))
}
