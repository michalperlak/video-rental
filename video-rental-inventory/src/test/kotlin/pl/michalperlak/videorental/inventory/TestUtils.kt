package pl.michalperlak.videorental.inventory

import arrow.core.getOrElse
import org.junit.jupiter.api.Assumptions.assumeTrue
import pl.michalperlak.videorental.inventory.domain.MovieCopiesRepository
import pl.michalperlak.videorental.inventory.domain.MoviesRepository
import pl.michalperlak.videorental.inventory.dto.NewMovie
import pl.michalperlak.videorental.inventory.dto.NewMovieCopy
import java.time.Clock
import java.time.LocalDate

internal fun createInventory(
    moviesRepository: MoviesRepository = InMemoryMoviesRepository(),
    movieCopiesRepository: MovieCopiesRepository = InMemoryMovieCopiesRepository(),
    clock: Clock = Clock.systemUTC()
): InventoryFacade =
    InventoryFacade(moviesRepository, movieCopiesRepository, clock)

internal fun InventoryFacade.addMovie(title: String, releaseDate: LocalDate): String {
    val addMovieResult = addMovie(NewMovie(title, releaseDate))
    assumeTrue(addMovieResult.isRight())
    return addMovieResult
        .getOrElse { throw IllegalStateException() }
        .id
}

internal fun InventoryFacade.addNewCopy(movieId: String): String {
    val addCopyResult = addNewCopy(NewMovieCopy(movieId))
    assumeTrue(addCopyResult.isRight())
    return addCopyResult
        .getOrElse { throw IllegalStateException() }
        .copyId
}