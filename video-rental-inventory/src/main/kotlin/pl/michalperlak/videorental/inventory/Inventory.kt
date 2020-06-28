package pl.michalperlak.videorental.inventory

import arrow.core.Either
import arrow.core.Option
import pl.michalperlak.videorental.inventory.dto.Movie
import pl.michalperlak.videorental.inventory.dto.MovieCopy
import pl.michalperlak.videorental.inventory.dto.NewMovie
import pl.michalperlak.videorental.inventory.dto.NewMovieCopy
import pl.michalperlak.videorental.inventory.error.MovieAddingError
import pl.michalperlak.videorental.inventory.error.MovieCopyAddingError

interface Inventory {
    fun addMovie(newMovie: NewMovie): Either<MovieAddingError, Movie>
    fun getMovie(movieId: String): Option<Movie>
    fun addNewCopy(newMovieCopy: NewMovieCopy): Either<MovieCopyAddingError, MovieCopy>
    fun getCopy(copyId: String): Option<MovieCopy>
}