package pl.michalperlak.videorental.web.bonuses

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.michalperlak.videorental.bonuses.Bonuses
import pl.michalperlak.videorental.common.events.Events

@Configuration
class BonusesConfig {

    @Bean
    fun bonuses(events: Events): Bonuses = Bonuses.configure(events)
}