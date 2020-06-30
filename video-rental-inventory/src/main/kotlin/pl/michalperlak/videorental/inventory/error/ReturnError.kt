package pl.michalperlak.videorental.inventory.error

import arrow.core.ListK

sealed class ReturnError

data class UnknownCopies(
    val copyIds: ListK<String>
) : ReturnError()

data class ErrorReturningCopies(
    val error: Throwable
) : ReturnError()