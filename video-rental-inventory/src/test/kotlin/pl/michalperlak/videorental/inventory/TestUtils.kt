package pl.michalperlak.videorental.inventory

import pl.michalperlak.videorental.inventory.domain.MovieCopiesRepository
import pl.michalperlak.videorental.inventory.domain.MoviesRepository
import java.time.Clock

fun createInventory(
    moviesRepository: MoviesRepository = InMemoryMoviesRepository(),
    movieCopiesRepository: MovieCopiesRepository = InMemoryMovieCopiesRepository(),
    clock: Clock = Clock.systemUTC()
): InventoryFacade =
    InventoryFacade(moviesRepository, movieCopiesRepository, clock)