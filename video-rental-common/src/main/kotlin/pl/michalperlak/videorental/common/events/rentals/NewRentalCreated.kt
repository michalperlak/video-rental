package pl.michalperlak.videorental.common.events.rentals

import arrow.core.ListK
import pl.michalperlak.videorental.common.domain.MovieType
import pl.michalperlak.videorental.common.events.Event
import java.time.Instant

data class NewRentalCreated(
    val customerId: String,
    val items: ListK<MovieType>,
    override val created: Instant
) : Event