package pl.michalperlak.videorental.web.events

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.michalperlak.videorental.common.events.Events
import pl.michalperlak.videorental.common.events.infr.InMemoryEventBus

@Configuration
class EventsConfig {

    @Bean
    fun events(): Events = InMemoryEventBus()
}