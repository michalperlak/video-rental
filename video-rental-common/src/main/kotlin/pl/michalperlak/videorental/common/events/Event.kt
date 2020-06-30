package pl.michalperlak.videorental.common.events

import java.time.Instant

interface Event {
    val created: Instant
}