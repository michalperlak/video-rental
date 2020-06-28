package pl.michalperlak.videorental.inventory.db

import io.kotest.assertions.arrow.option.shouldBeSome
import io.kotest.matchers.longs.shouldBeExactly
import io.kotest.matchers.sequences.shouldContainAll
import io.kotest.matchers.sequences.shouldHaveCount
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import pl.michalperlak.videorental.inventory.domain.MovieCopy
import pl.michalperlak.videorental.inventory.domain.MovieCopyId
import pl.michalperlak.videorental.inventory.domain.MovieCopyStatus
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
            added = Instant.ofEpochMilli(timestampEpochMillis),
            status = MovieCopyStatus.AVAILABLE
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
        // given
        val copy = MovieCopy(
            id = MovieCopyId.generate(),
            movieId = MovieId.generate(),
            added = Instant.ofEpochMilli(25678902),
            status = MovieCopyStatus.AVAILABLE
        )
        movieCopiesRepository.addCopy(copy)

        // when
        val result = movieCopiesRepository.findById(copy.id)

        // then
        result shouldBeSome copy
    }

    "should find copies by movie id and status" {
        // given
        val movieId = MovieId.generate()
        val status = MovieCopyStatus.AVAILABLE
        val copy1 = MovieCopy(
            id = MovieCopyId.generate(),
            movieId = movieId,
            added = Instant.ofEpochMilli(25678902),
            status = status
        )
        val copy2 = MovieCopy(
            id = MovieCopyId.generate(),
            movieId = movieId,
            added = Instant.ofEpochMilli(25678909),
            status = status
        )
        movieCopiesRepository.addCopy(copy1)
        movieCopiesRepository.addCopy(copy2)

        // when
        val result = movieCopiesRepository.findByMovieAndStatus(movieId, status)

        // then
        transaction(database) {
            result shouldHaveCount 2
            result shouldContainAll listOf(copy1, copy2)
        }
    }
})