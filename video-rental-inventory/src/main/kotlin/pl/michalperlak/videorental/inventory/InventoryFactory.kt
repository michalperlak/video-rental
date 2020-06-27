package pl.michalperlak.videorental.inventory

import org.jetbrains.exposed.sql.Database
import pl.michalperlak.videorental.inventory.db.ExposedMovieCopiesRepository
import pl.michalperlak.videorental.inventory.db.ExposedMoviesRepository
import java.time.Clock
import javax.sql.DataSource

object InventoryFactory {
    fun create(dataSource: DataSource, clock: Clock): Inventory {
        val database = Database.connect(dataSource)
        val moviesRepository = ExposedMoviesRepository(database)
        val movieCopiesRepository = ExposedMovieCopiesRepository(database)
        return InventoryFacade(moviesRepository, movieCopiesRepository, clock)
    }
}