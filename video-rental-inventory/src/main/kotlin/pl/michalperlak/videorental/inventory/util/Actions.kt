package pl.michalperlak.videorental.inventory.util

import arrow.core.Either
import arrow.core.nonFatalOrThrow

internal fun <L, R> execute(callable: () -> Either<L, R>, errorHandler: (Throwable) -> L): Either<L, R> =
    try {
        callable()
    } catch (e: Exception) {
        Either.left(errorHandler(e.nonFatalOrThrow()))
    }