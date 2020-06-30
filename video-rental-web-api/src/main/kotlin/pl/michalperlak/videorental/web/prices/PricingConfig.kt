package pl.michalperlak.videorental.web.prices

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.michalperlak.videorental.pricing.Prices
import pl.michalperlak.videorental.pricing.api.Price
import pl.michalperlak.videorental.pricing.classification.MovieClassificationPolicy
import java.time.Clock
import java.time.Period

@Configuration
class PricingConfig {

    @Bean
    fun prices(): Prices = Prices.createInstance(regularBase = REGULAR_BASE, premiumBase = PREMIUM_BASE)

    @Bean
    fun classificationPolicy(clock: Clock): MovieClassificationPolicy =
        MovieClassificationPolicy.default(
            newReleaseMaxAge = NEW_RELEASE_MAX_AGE, oldMovieMinAge = OLD_MOVIE_MIN_AGE, clock = clock
        )

    companion object {
        private val PREMIUM_BASE = Price.of(40)
        private val REGULAR_BASE = Price.of(30)

        private val NEW_RELEASE_MAX_AGE = Period.ofDays(50)
        private val OLD_MOVIE_MIN_AGE = Period.ofYears(10)
    }
}