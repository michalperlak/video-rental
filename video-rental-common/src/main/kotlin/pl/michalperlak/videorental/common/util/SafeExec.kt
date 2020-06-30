package pl.michalperlak.videorental.common.util

import arrow.core.Either
import arrow.core.nonFatalOrThrow

fun <L, R> executeForEither(callable: () -> Either<L, R>, errorHandler: (Throwable) -> L): Either<L, R> =
    try {
        callable()
    } catch (e: Exception) {
        Either.left(errorHandler(e.nonFatalOrThrow()))
    }

fun <L, R> executeAndHandle(callable: () -> R, errorHandler: (Throwable) -> L): Either<L, R> =
    try {
        Either.right(callable())
    } catch (e: Exception) {
        Either.left(errorHandler(e.nonFatalOrThrow()))
    }