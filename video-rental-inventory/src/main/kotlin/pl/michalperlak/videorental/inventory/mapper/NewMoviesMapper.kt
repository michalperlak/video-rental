package pl.michalperlak.videorental.inventory.mapper

import pl.michalperlak.videorental.inventory.domain.Movie
import pl.michalperlak.videorental.inventory.domain.MovieId
import pl.michalperlak.videorental.inventory.dto.NewMovie

internal fun NewMovie.createMovie(movieId: MovieId): Movie =
    Movie(id = movieId, title = title, releaseDate = releaseDate)