package pl.michalperlak.videorental.inventory.domain

import arrow.core.ListK

internal data class Rental(
    val copies: ListK<MovieCopy>
)