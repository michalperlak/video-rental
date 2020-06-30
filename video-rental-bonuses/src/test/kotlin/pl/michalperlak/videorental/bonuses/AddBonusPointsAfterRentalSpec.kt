package pl.michalperlak.videorental.bonuses

import arrow.core.k
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeExactly
import pl.michalperlak.videorental.common.domain.MovieType
import pl.michalperlak.videorental.common.events.Events
import pl.michalperlak.videorental.common.events.infr.InMemoryEventBus
import pl.michalperlak.videorental.common.events.rentals.NewRentalCreated
import java.time.Instant
import java.util.UUID

class AddBonusPointsAfterRentalSpec : StringSpec({

    "should add correct points for new release rental" {
        // given
        val (bonuses, events) = configure()
        val customerId = UUID.randomUUID().toString()
        val newRentalEvent = NewRentalCreated(customerId, listOf(MovieType.NEW_RELEASE).k(), Instant.now())

        // when
        events.publish(newRentalEvent)

        // then
        val customerPoints = bonuses.getCustomerPoints(customerId)
        customerPoints shouldBeExactly 2
    }

    "should add correct points for regular movie rental" {
        // given
        val (bonuses, events) = configure()
        val customerId = UUID.randomUUID().toString()
        val newRentalEvent = NewRentalCreated(customerId, listOf(MovieType.REGULAR_MOVIE).k(), Instant.now())

        // when
        events.publish(newRentalEvent)

        // then
        val customerPoints = bonuses.getCustomerPoints(customerId)
        customerPoints shouldBeExactly 1
    }

    "should add correct points for old movie rental" {
        // given
        val (bonuses, events) = configure()
        val customerId = UUID.randomUUID().toString()
        val newRentalEvent = NewRentalCreated(customerId, listOf(MovieType.OLD_MOVIE).k(), Instant.now())

        // when
        events.publish(newRentalEvent)

        // then
        val customerPoints = bonuses.getCustomerPoints(customerId)
        customerPoints shouldBeExactly 1
    }

    "should add correct points for mixed rental" {
        // given
        val (bonuses, events) = configure()
        val customerId = UUID.randomUUID().toString()
        val newRentalEvent = NewRentalCreated(
            customerId, listOf(MovieType.NEW_RELEASE, MovieType.REGULAR_MOVIE, MovieType.OLD_MOVIE).k(), Instant.now())

        // when
        events.publish(newRentalEvent)

        // then
        val customerPoints = bonuses.getCustomerPoints(customerId)
        customerPoints shouldBeExactly 4
    }
})

private fun configure(): Pair<Bonuses, Events> {
    val events = InMemoryEventBus()
    val bonuses = Bonuses.configure(events)
    return bonuses to events
}