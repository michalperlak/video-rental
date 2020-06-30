package pl.michalperlak.videorental.web.rentals

import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.When
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import pl.michalperlak.videorental.web.inventory.MovieCopiesController

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