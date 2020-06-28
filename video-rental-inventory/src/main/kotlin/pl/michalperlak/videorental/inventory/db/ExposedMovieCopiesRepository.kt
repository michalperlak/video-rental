package pl.michalperlak.videorental.inventory.db

import arrow.core.Option
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import pl.michalperlak.videorental.inventory.domain.MovieCopiesRepository
import pl.michalperlak.videorental.inventory.domain.MovieCopy
import pl.michalperlak.videorental.inventory.domain.MovieCopyId
import pl.michalperlak.videorental.inventory.domain.MovieId
import java.time.Instant

internal class ExposedMovieCopiesRepository(
    private val database: Database
) : MovieCopiesRepository {
    override fun addCopy(movieCopy: MovieCopy): MovieCopy = transaction(database) {
        movieCopy.apply {
            MovieCopies.insert {
                it[id] = movieCopy.id.asUUID()
                it[movieId] = movieCopy.movieId.asUUID()
                it[added] = movieCopy.added.toEpochMilli()
            }
        }
    }

    override fun findById(copyId: MovieCopyId): Option<MovieCopy> = transaction(database) {
        Option.fromNullable(
            MovieCopies.select { MovieCopies.id.eq(copyId.asUUID()) }
                .map {
                    MovieCopy(
                        id = MovieCopyId(it[MovieCopies.id]),
                        movieId = MovieId(it[MovieCopies.movieId]),
                        added = Instant.ofEpochMilli(it[MovieCopies.added])
                    )
                }
                .firstOrNull()
        )
    }

}

internal object MovieCopies : Table("MOVIE_COPIES") {
    val id = uuid("uuid")
    val movieId = uuid("movie_id")
    val added = long("added_epoch_millis")
}