package pl.michalperlak.videorental.inventory

import arrow.core.Either
import arrow.core.ListK
import arrow.core.Option
import pl.michalperlak.videorental.inventory.dto.Movie
import pl.michalperlak.videorental.inventory.dto.MovieCopy
import pl.michalperlak.videorental.inventory.dto.MovieToRent
import pl.michalperlak.videorental.inventory.dto.NewMovie
import pl.michalperlak.videorental.inventory.dto.NewMovieCopy
import pl.michalperlak.videorental.inventory.dto.Rental
import pl.michalperlak.videorental.inventory.error.MovieAddingError
import pl.michalperlak.videorental.inventory.error.MovieCopyAddingError
import pl.michalperlak.videorental.inventory.error.RentalInventoryError
import pl.michalperlak.videorental.inventory.error.ReturnError

interface Inventory {
    fun addMovie(newMovie: NewMovie): Either<MovieAddingError, Movie>
    fun getMovie(movieId: String): Option<Movie>
    fun addNewCopy(newMovieCopy: NewMovieCopy): Either<MovieCopyAddingError, MovieCopy>
    fun getCopy(copyId: String): Option<MovieCopy>
    fun rentMovies(movies: ListK<MovieToRent>): Either<RentalInventoryError, Rental>
    fun returnCopies(movieCopyIds: ListK<String>): Either<ReturnError, Unit>
}