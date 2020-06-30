package pl.michalperlak.videorental.inventory.mapper

import pl.michalperlak.videorental.inventory.domain.MovieId
import pl.michalperlak.videorental.inventory.domain.Rental
import pl.michalperlak.videorental.inventory.dto.RentedCopy
import java.time.LocalDate
import pl.michalperlak.videorental.inventory.dto.Rental as RentalDto

internal fun Rental.asDto(movieReleaseDateSupplier: (MovieId) -> LocalDate): RentalDto =
    RentalDto(copies = copies.map {
        RentedCopy(
            copyId = it.id.toString(),
            movieId = it.movieId.toString(),
            movieReleaseDate = movieReleaseDateSupplier(it.movieId)
        )
    })