package pl.michalperlak.videorental.inventory.db

import arrow.core.Option
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.date
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import pl.michalperlak.videorental.inventory.domain.Movie
import pl.michalperlak.videorental.inventory.domain.MovieId
import pl.michalperlak.videorental.inventory.domain.MoviesRepository
import java.time.LocalDate
import java.util.UUID

internal class ExposedMoviesRepository(
    private val moviesDatabase: Database
) : MoviesRepository {
    override fun addMovie(movie: Movie): Movie = transaction(moviesDatabase) {
        movie.apply {
            Movies.insert {
                it[id] = movie.id.asUUID()
                it[title] = movie.title
                it[releaseDate] = movie.releaseDate
            }
        }
    }

    override fun findById(movieId: MovieId): Option<Movie> = transaction(moviesDatabase) {
        findMovie { Movies.id.eq(movieId.asUUID()) }
    }

    override fun findMovie(title: String, releaseDate: LocalDate): Option<Movie> = transaction(moviesDatabase) {
        findMovie {
            Movies.releaseDate.eq(releaseDate) and Movies.title.lowerCase().eq(title.toLowerCase())
        }
    }

    private fun findMovie(predicate: SqlExpressionBuilder.() -> Op<Boolean>): Option<Movie> =
        Option.fromNullable(
            Movies
                .select(predicate)
                .map {
                    Movie(
                        id = it[Movies.id].toMovieId(),
                        title = it[Movies.title],
                        releaseDate = it[Movies.releaseDate]
                    )
                }
                .firstOrNull()
        )

    private fun UUID.toMovieId(): MovieId = MovieId(this)
}

internal object Movies : Table("MOVIES") {
    val id = uuid("uuid")
    val title = varchar("title", 250)
    val releaseDate = date("release_date")
}