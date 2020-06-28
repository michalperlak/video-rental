package pl.michalperlak.videorental.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties
@SpringBootApplication
class VideoRentalApplication

fun main(args: Array<String>) {
    runApplication<VideoRentalApplication>(*args)
}