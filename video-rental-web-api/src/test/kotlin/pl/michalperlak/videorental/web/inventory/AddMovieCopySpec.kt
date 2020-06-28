package pl.michalperlak.videorental.web.inventory

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.StringSpec
import io.kotest.spring.SpringListener
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.Matchers.startsWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

@SpringBootTest(webEnvironment = RANDOM_PORT)
class AddMovieCopySpec : StringSpec() {

    @LocalServerPort
    private var port: Int = 0

    init {
        "should return created status with location when copy added successfully" {
            val movieId = addMovie(port, """{ "title" : "Test", "releaseDate": "2020-06-25" }""")
                .substringAfter(MoviesController.MOVIES_PATH + "/")
            Given {
                port(port)
                body(
                    """{ "movieId": "$movieId" }""".trimIndent()
                )
                header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            } When {
                post(MovieCopiesController.MOVIE_COPIES_PATH)
            } Then {
                statusCode(201)
                header(HttpHeaders.LOCATION, startsWith(MovieCopiesController.MOVIE_COPIES_PATH))
            }

        }

        "should return not found status when movie does not exist" {
            Given {
                port(port)
                body(
                    """{ "movieId": "abcd-1234" }""".trimIndent()
                )
                header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            } When {
                post(MovieCopiesController.MOVIE_COPIES_PATH)
            } Then {
                statusCode(404)
            }
        }

        "should return bad request status when copy data is incomplete" {
            Given {
                port(port)
                body(
                    """{}"""
                )
                header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            } When {
                post(MovieCopiesController.MOVIE_COPIES_PATH)
            } Then {
                statusCode(400)
            }
        }
    }

    override fun listeners(): List<TestListener> = listOf(SpringListener)
}