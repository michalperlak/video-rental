package pl.michalperlak.videorental.web.inventory

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
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
    }

val mapper: ObjectMapper = ObjectMapper()
    .registerKotlinModule()
    .findAndRegisterModules()

inline fun <reified T : Any> ExtractableResponse<Response>.extractBody(): T {
    val content = body().asString()
    return mapper.readValue(content, T::class.java)
}