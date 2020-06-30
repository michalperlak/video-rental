package pl.michalperlak.videorental.web.rentals

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.michalperlak.videorental.common.events.Events
import pl.michalperlak.videorental.inventory.Inventory
import pl.michalperlak.videorental.pricing.Prices
import pl.michalperlak.videorental.pricing.classification.MovieClassificationPolicy
import pl.michalperlak.videorental.rentals.Rentals
import java.time.Clock

@Configuration
class RentalsConfig {

    @Bean
    fun rentals(
        prices: Prices, movieClassificationPolicy: MovieClassificationPolicy,
        inventory: Inventory, events: Events, clock: Clock
    ): Rentals = Rentals.create(prices, movieClassificationPolicy, inventory, events, clock)
}