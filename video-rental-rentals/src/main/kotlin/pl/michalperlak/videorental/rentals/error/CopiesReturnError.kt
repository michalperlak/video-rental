package pl.michalperlak.videorental.rentals.error

import arrow.core.ListK

sealed class CopiesReturnError

data class RentalNotRecognized(
    val rentalId: String
) : CopiesReturnError()

data class CopiesNotRecognized(
    val ids: ListK<String>
) : CopiesReturnError()

data class ErrorDuringReturn(
    val error: Throwable
) : CopiesReturnError()