package pl.michalperlak.videorental.pricing.domain

import arrow.core.ListK

data class DelayedReturn(
    val items: ListK<LateReturnedItem>
)
