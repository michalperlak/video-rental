package pl.michalperlak.videorental.inventory.mapper

import pl.michalperlak.videorental.inventory.domain.Movie
import pl.michalperlak.videorental.inventory.dto.Movie as MovieDto

fun Movie.asDto(): MovieDto = MovieDto(id = id.toString(), title = title, releaseDate = releaseDate)