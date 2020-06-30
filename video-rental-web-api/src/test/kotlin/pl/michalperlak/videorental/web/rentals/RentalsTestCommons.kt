package pl.michalperlak.videorental.web.rentals

import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.When
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import pl.michalperlak.videorental.web.inventory.MovieCopiesController
import pl.michalperlak.videorental.web.inventory.addMovie
import java.util.UUID

fun addCopy(port: Int, movieId: String): String =
    Given {
        port(port)
        body(""" { "movieId": "$movieId" }""")
        header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    } When {
        post(MovieCopiesController.MOVIE_COPIES_PATH)
    } Extract {
        header(HttpHeaders.LOCATION)
            .substringAfter(MovieCopiesController.MOVIE_COPIES_PATH + "/")
    }

fun addRental(port: Int, customerId: String = UUID.randomUUID().toString()): Pair<String, String> {
    val movieId = addMovie(port, """ { "title": "Test movie", "releaseDate": "2020-05-12" }""")
    val copyId = addCopy(port, movieId)
    return Given {
        port(port)
        body(
            """
                    {
                        "customerId": "$customerId",
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