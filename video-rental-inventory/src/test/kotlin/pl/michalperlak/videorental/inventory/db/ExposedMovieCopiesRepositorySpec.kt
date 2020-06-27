package pl.michalperlak.videorental.inventory.db

import io.kotest.matchers.longs.shouldBeExactly
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import pl.michalperlak.videorental.inventory.domain.MovieCopy
import pl.michalperlak.videorental.inventory.domain.MovieCopyId
import pl.michalperlak.videorental.inventory.domain.MovieId
import java.time.Instant

class ExposedMovieCopiesRepositorySpec : InMemoryDatabaseSpec(tables = listOf(MovieCopies), dbName = "copies", body = {
    val movieCopiesRepository = ExposedMovieCopiesRepository(database)

    "should save movie copy in the repository" {
        // given
        val timestampEpochMillis = 123456789L
        val copy = MovieCopy(
            id = MovieCopyId.generate(),
            movieId = MovieId.generate(),
            added = Instant.ofEpochMilli(timestampEpochMillis)
        )

        // when
        movieCopiesRepository.addCopy(copy)

        // then
        val records = transaction(database) {
            MovieCopies.select {
                MovieCopies.id.eq(copy.id.asUUID())
                    .and(
                        MovieCopies.movieId.eq(copy.movieId.asUUID())
                    )
                    .and(
                        MovieCopies.added.eq(timestampEpochMillis)
                    )
            }.count()
        }
        records shouldBeExactly 1
    }

    "should find movie copy by id" {

    }
})