package pl.michalperlak.videorental.web.common

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
class TimeConfig {

    @Bean
    fun applicationClock(): Clock = Clock.systemUTC()
}