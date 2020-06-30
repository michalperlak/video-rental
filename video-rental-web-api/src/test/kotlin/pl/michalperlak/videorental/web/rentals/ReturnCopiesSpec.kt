package pl.michalperlak.videorental.web.rentals

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.StringSpec
import io.kotest.spring.SpringListener
import io.restassured.module.kotlin.extensions.Extract
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
class ReturnCopiesSpec : StringSpec() {

    @LocalServerPort
    private var port: Int = 0

    init {
        "should return bad request when rental id not provided" {
            Given {
                port(port)
                body(
                    """
                    { "copies": ["${UUID.randomUUID()}"] }
                """.trimIndent()
                )
                header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            } When {
                post(ReturnsController.RETURNS_PATH)
            } Then {
                statusCode(400)
            }
        }

        "should return bad request when copies to return not provided" {
            Given {
                port(port)
                body(
                    """
                    { "rentalId": "${UUID.randomUUID()}" }
                """.trimIndent()
                )
                header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            } When {
                post(ReturnsController.RETURNS_PATH)
            } Then {
                statusCode(400)
            }
        }

        "should return not found status when rental does not exist" {
            Given {
                port(port)
                body(
                    """
                    { "rentalId": "${UUID.randomUUID()}", "copies": ["${UUID.randomUUID()}"] }
                """.trimIndent()
                )
                header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            } When {
                post(ReturnsController.RETURNS_PATH)
            } Then {
                statusCode(404)
            }
        }

        "should return summary with additional charges when items returned successfully" {
            val (rentalId, copyId) = addRental()
            Given {
                port(port)
                body(
                    """
                    { "rentalId": "$rentalId", "copies": ["$copyId"] }
                """.trimIndent()
                )
                header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            } When {
                post(ReturnsController.RETURNS_PATH)
            } Then {
                statusCode(200)
                body(containsString("delayCharge"))
            }
        }
    }

    private fun addRental(): Pair<String, String> {
        val movieId = addMovie(port, """ { "title": "Test movie", "releaseDate": "2020-05-12" }""")
        val copyId = addCopy(port, movieId)
        return Given {
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
        } Extract {
            header(HttpHeaders.LOCATION)
                .substringAfter(RentalsController.RENTALS_PATH + "/")
        } to copyId
    }

    override fun listeners(): List<TestListener> = listOf(SpringListener)
}