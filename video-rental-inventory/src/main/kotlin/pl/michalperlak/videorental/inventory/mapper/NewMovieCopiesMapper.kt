package pl.michalperlak.videorental.inventory.mapper

import arrow.core.Option
import pl.michalperlak.videorental.inventory.domain.MovieCopy
import pl.michalperlak.videorental.inventory.domain.MovieCopyId
import pl.michalperlak.videorental.inventory.domain.MovieId
import pl.michalperlak.videorental.inventory.dto.NewMovieCopy
import java.time.Instant

fun NewMovieCopy.createMovieCopy(copyId: MovieCopyId, additionTimestamp: Instant): Option<MovieCopy> =
    MovieId
        .from(movieId)
        .map { MovieCopy(copyId, it, additionTimestamp) }