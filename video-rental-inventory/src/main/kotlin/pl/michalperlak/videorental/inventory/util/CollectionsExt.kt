package pl.michalperlak.videorental.inventory.util

import arrow.core.ListK
import arrow.core.Option
import arrow.core.k

fun <T1, T2> ListK<T1>.flatMapOption(mapper: (T1) -> Option<T2>): ListK<T2> =
    flatMap {
        mapper(it)
            .toList()
            .k()
    }