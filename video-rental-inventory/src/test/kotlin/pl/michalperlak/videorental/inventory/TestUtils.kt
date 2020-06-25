package pl.michalperlak.videorental.inventory

import pl.michalperlak.videorental.inventory.domain.MovieCopiesRepository
import pl.michalperlak.videorental.inventory.domain.MoviesRepository

fun createInventory(
    moviesRepository: MoviesRepository = InMemoryMoviesRepository(),
    movieCopiesRepository: MovieCopiesRepository = InMemoryMovieCopiesRepository()
): InventoryFacade =
    InventoryFacade(moviesRepository, movieCopiesRepository)