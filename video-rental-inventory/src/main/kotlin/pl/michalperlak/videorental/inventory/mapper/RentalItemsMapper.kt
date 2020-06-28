package pl.michalperlak.videorental.inventory.mapper

import arrow.core.Option
import pl.michalperlak.videorental.inventory.domain.MovieId
import pl.michalperlak.videorental.inventory.domain.RentalItem
import pl.michalperlak.videorental.inventory.dto.MovieToRent

internal fun MovieToRent.toRentalItem(): Option<RentalItem> =
    MovieId
        .from(movieId)
        .map { RentalItem(movieId = it, copies = copies) }