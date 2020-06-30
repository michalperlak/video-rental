package pl.michalperlak.videorental.common.events

typealias Handler<T> = (T) -> Unit

interface Events {
    fun <T : Event> subscribe(type: Class<T>, handler: Handler<T>)
    fun publish(event: Event)
}

inline fun <reified T : Event> Events.subscribe(noinline handler: (T) -> Unit) = subscribe(T::class.java, handler)