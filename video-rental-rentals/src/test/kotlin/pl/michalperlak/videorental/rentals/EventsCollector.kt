package pl.michalperlak.videorental.rentals

import pl.michalperlak.videorental.common.events.Event
import pl.michalperlak.videorental.common.events.Events
import pl.michalperlak.videorental.common.events.Handler
import pl.michalperlak.videorental.common.events.infr.InMemoryEventBus

class EventsCollector : Events {
    private val eventBus = InMemoryEventBus()
    private val collected = ArrayList<Event>()

    override fun <T : Event> subscribe(type: Class<T>, handler: Handler<T>) {
        eventBus.subscribe(type, handler)
    }

    override fun publish(event: Event) {
        collected.add(event)
        eventBus.publish(event)
    }

    fun getCollected(): List<Event> = collected.toList()
}