package pl.michalperlak.videorental.web.rentals

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.StringSpec
import io.kotest.spring.SpringListener
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.core.StringContains.containsString
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import pl.michalperlak.videorental.web.inventory.addMovie
import java.util.UUID

@SpringBootTest(webEnvironment = RANDOM_PORT)
class RentMoviesSpec : StringSpec() {

    @LocalServerPort
    private var port: Int = 0

    init {
        "should return bad request status when rental data is incomplete" {
            Given {
                port(port)
                body(
                    """
                    {
                        "customerId": "${UUID.randomUUID()}",
                         "items": [
                            { "movieId": "f8579e3a-7f20-44c2-a2af-eeee5a59c19b" }
                        ]
                    }
                """.trimIndent()
                )
                header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            } When {
                post(RentalsController.RENTALS_PATH)
            } Then {
                statusCode(400)
            }
        }

        "should return client error when movie is not available for rent" {
            val movieId = addMovie(port, """ { "title": "Test movie", "releaseDate": "2020-05-12" }""")
            Given {
                port(port)
                body(
                    """
                    {
                        "customerId": "${UUID.randomUUID()}",
                         "items": [
                            { "movieId": "$movieId", "days": 2 }
                        ]
                    }
                """.trimIndent()
                )
                header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            } When {
                post(RentalsController.RENTALS_PATH)
            } Then {
                statusCode(400)
            }
        }

        "should return ok status with rental data when rental created" {
            val movieId = addMovie(port, """ { "title": "Test movie", "releaseDate": "2020-05-12" }""")
            repeat(2) {
                addCopy(port, movieId)
            }
            Given {
                port(port)
                body(
                    """
                    {
                        "customerId": "${UUID.randomUUID()}",
                         "items": [
                            { "movieId": "$movieId", "days": 2 },
                            { "movieId": "$movieId", "days": 10 }
                        ]
                    }
                """.trimIndent()
                )
                header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            } When {
                post(RentalsController.RENTALS_PATH)
            } Then {
                statusCode(200)
                body(containsString("totalPrice"))
                body(containsString("items"))
                body(containsString("startDate"))
                body(containsString("rentalId"))
            }
        }
    }

    override fun listeners(): List<TestListener> = listOf(SpringListener)
}