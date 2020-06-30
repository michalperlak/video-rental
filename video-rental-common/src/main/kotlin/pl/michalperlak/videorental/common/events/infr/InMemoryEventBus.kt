package pl.michalperlak.videorental.common.events.infr

import pl.michalperlak.videorental.common.events.Event
import pl.michalperlak.videorental.common.events.Events
import pl.michalperlak.videorental.common.events.Handler
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.CopyOnWriteArrayList

class InMemoryEventBus : Events {
    private val subscribers: ConcurrentMap<String, MutableList<Handler<Event>>> = ConcurrentHashMap()

    @Suppress("UNCHECKED_CAST")
    @Synchronized
    override fun <T : Event> subscribe(type: Class<T>, handler: (T) -> Unit) {
        val typeSubscribers = subscribers.getOrDefault(type.name, CopyOnWriteArrayList())
        typeSubscribers.add { handler(it as T) }
        subscribers[type.name] = typeSubscribers
    }

    override fun publish(event: Event) {
        val eventSubscribers = subscribers[event.javaClass.name] ?: return
        eventSubscribers.forEach { it(event) }
    }
}