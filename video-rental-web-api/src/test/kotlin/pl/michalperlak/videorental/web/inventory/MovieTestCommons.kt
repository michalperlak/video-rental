package pl.michalperlak.videorental.web.inventory

import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.When
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

fun addMovie(port: Int, newMovieBody: String): String =
    Given {
        port(port)
        body(newMovieBody)
        header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    } When {
        post(MoviesController.MOVIES_PATH)
    } Extract {
        header(HttpHeaders.LOCATION)
            .substringAfter(MoviesController.MOVIES_PATH + "/")
    }