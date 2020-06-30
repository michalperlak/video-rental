package pl.michalperlak.videorental.rentals

import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

internal class MutableClock(
    var currentDate: LocalDate
) : Clock() {
    override fun withZone(zone: ZoneId): Clock = this

    override fun getZone(): ZoneId = ZoneOffset.UTC

    override fun instant(): Instant = LocalDateTime
        .of(currentDate, LocalTime.NOON)
        .toInstant(ZoneOffset.UTC)
}