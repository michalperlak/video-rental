package pl.michalperlak.videorental.inventory

import org.jetbrains.exposed.sql.Database
import pl.michalperlak.videorental.common.transactions.exposed.ExposedTransactionsHandler
import pl.michalperlak.videorental.inventory.application.DefaultMovieRentalService
import pl.michalperlak.videorental.inventory.db.ExposedMovieCopiesRepository
import pl.michalperlak.videorental.inventory.db.ExposedMoviesRepository
import java.time.Clock
import javax.sql.DataSource

object InventoryFactory {
    fun create(dataSource: DataSource, clock: Clock): Inventory {
        val database = Database.connect(dataSource)
        database.useNestedTransactions = true
        val moviesRepository = ExposedMoviesRepository(database)
        val movieCopiesRepository = ExposedMovieCopiesRepository(database)
        val transactionsHandler = ExposedTransactionsHandler(database)
        val movieRentalService = DefaultMovieRentalService(movieCopiesRepository, transactionsHandler)
        return InventoryFacade(moviesRepository, movieCopiesRepository, movieRentalService, clock)
    }
}