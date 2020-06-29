package pl.michalperlak.videorental.rentals.mapper

import pl.michalperlak.videorental.inventory.dto.MovieToRent
import pl.michalperlak.videorental.rentals.dto.NewRentalItem

internal fun NewRentalItem.forInventory(): MovieToRent = MovieToRent(movieId = movieId, copies = copies)