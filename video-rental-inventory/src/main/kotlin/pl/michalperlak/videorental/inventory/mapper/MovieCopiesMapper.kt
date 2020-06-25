package pl.michalperlak.videorental.inventory.mapper

import pl.michalperlak.videorental.inventory.domain.MovieCopy
import pl.michalperlak.videorental.inventory.dto.MovieCopy as MovieCopyDto

fun MovieCopy.asDto(): MovieCopyDto = MovieCopyDto(copyId = id.toString(), movieId = movieId.toString(), added = added)