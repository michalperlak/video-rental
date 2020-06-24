package pl.michalperlak.videorental.inventory.util

import arrow.core.Either
import arrow.core.nonFatalOrThrow

fun <R> Either.Companion.of(callable: () -> R): Either<Throwable, R> =
    try {
        right(callable())
    } catch (e: Exception) {
        left(e.nonFatalOrThrow())
    }