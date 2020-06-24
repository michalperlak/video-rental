package pl.michalperlak.videorental.pricing.domain

import arrow.core.ListK
import arrow.core.combineK

data class DelayedReturn(
    val items: ListK<LateReturnedItem>
) {
    operator fun plus(other: DelayedReturn): DelayedReturn =
        DelayedReturn(items.combineK(other.items))
}
