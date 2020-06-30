package pl.michalperlak.videorental.rentals.mapper

import pl.michalperlak.videorental.pricing.api.MovieType
import pl.michalperlak.videorental.pricing.api.Rental
import pl.michalperlak.videorental.pricing.api.RentalItem
import pl.michalperlak.videorental.rentals.dto.NewRental

internal fun NewRental.forPricing(): Rental =
    Rental(items = items.map {
        RentalItem(MovieType.REGULAR_MOVIE, it.duration) //TODO movie type
    })