package pl.michalperlak.videorental.inventory.domain

import arrow.core.Option
import java.time.LocalDate

interface MoviesRepository {
    fun addMovie(movie: Movie): Movie
    fun findMovie(title: String, releaseDate: LocalDate): Option<Movie>
}