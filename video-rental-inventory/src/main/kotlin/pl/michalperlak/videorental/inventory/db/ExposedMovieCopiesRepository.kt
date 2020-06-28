package pl.michalperlak.videorental.inventory.db

import arrow.core.Option
import arrow.core.SequenceK
import arrow.core.k
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import pl.michalperlak.videorental.inventory.domain.MovieCopiesRepository
import pl.michalperlak.videorental.inventory.domain.MovieCopy
import pl.michalperlak.videorental.inventory.domain.MovieCopyId
import pl.michalperlak.videorental.inventory.domain.MovieCopyStatus
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
                it[status] = movieCopy.status
            }
        }
    }

    override fun findById(copyId: MovieCopyId): Option<MovieCopy> = transaction(database) {
        Option.fromNullable(
            MovieCopies.select { MovieCopies.id.eq(copyId.asUUID()) }
                .map { it.movieCopy() }
                .firstOrNull()
        )
    }

    override fun findByMovieAndStatus(movieId: MovieId, status: MovieCopyStatus): SequenceK<MovieCopy> =
        transaction(database) {
            MovieCopies
                .select {
                    MovieCopies.movieId.eq(movieId.asUUID()) and MovieCopies.status.eq(status)
                }
                .asSequence()
                .map { it.movieCopy() }
                .k()
        }

    private fun ResultRow.movieCopy(): MovieCopy =
        MovieCopy(
            id = MovieCopyId(this[MovieCopies.id]),
            movieId = MovieId(this[MovieCopies.movieId]),
            added = Instant.ofEpochMilli(this[MovieCopies.added]),
            status = this[MovieCopies.status]
        )
}

internal object MovieCopies : Table("MOVIE_COPIES") {
    val id = uuid("uuid")
    val movieId = uuid("movie_id")
    val added = long("added_epoch_millis")
    val status = enumerationByName("status", 30, MovieCopyStatus::class)
}