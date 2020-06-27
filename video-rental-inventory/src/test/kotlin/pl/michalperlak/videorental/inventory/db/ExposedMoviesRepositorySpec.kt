package pl.michalperlak.videorental.inventory.db

import io.kotest.assertions.arrow.option.shouldBeSome
import io.kotest.matchers.longs.shouldBeExactly
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import pl.michalperlak.videorental.inventory.domain.Movie
import pl.michalperlak.videorental.inventory.domain.MovieId
import java.time.LocalDate
import java.time.Month

class ExposedMoviesRepositorySpec : InMemoryDatabaseSpec(tables = listOf(Movies), dbName = "movies", body = {
    val moviesRepository = ExposedMoviesRepository(database)

    "should save movie in the database" {
        // given
        val movie = Movie(
            id = MovieId.generate(),
            title = "Test movie",
            releaseDate = LocalDate.of(2020, Month.JUNE, 27)
        )

        // when
        moviesRepository.addMovie(movie)

        // then
        val records = transaction(database) {
            Movies.select {
                Movies.id.eq(movie.id.asUUID()) and Movies.title.eq(movie.title) and Movies.releaseDate.eq(movie.releaseDate)
            }.count()
        }
        records shouldBeExactly 1
    }

    "should find saved movie by id" {
        // given
        val movie = Movie(
            id = MovieId.generate(),
            title = "Test movie 2",
            releaseDate = LocalDate.of(2020, Month.JUNE, 27)
        )
        moviesRepository.addMovie(movie)

        // when
        val result = moviesRepository.findById(movie.id)

        // then
        result shouldBeSome movie
    }

    "should find movie by title and release date - exact match" {
        // given
        val movie = Movie(
            id = MovieId.generate(),
            title = "Test movie 3",
            releaseDate = LocalDate.of(2020, Month.JUNE, 27)
        )
        moviesRepository.addMovie(movie)

        // when
        val result = moviesRepository.findMovie(title = movie.title, releaseDate = movie.releaseDate)

        // then
        result shouldBeSome movie
    }

    "should find movie by title and release date - ignore title case" {
        // given
        val movie = Movie(
            id = MovieId.generate(),
            title = "Test MoVie 4",
            releaseDate = LocalDate.of(2020, Month.JUNE, 27)
        )
        moviesRepository.addMovie(movie)

        // when
        val result = moviesRepository.findMovie(title = movie.title.toUpperCase(), releaseDate = movie.releaseDate)

        // then
        result shouldBeSome movie
    }
})