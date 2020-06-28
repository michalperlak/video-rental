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
class AddMovieSpec : StringSpec() {

    @LocalServerPort
    private var port: Int = 0

    init {
        "should return created status with location when movie added" {
            Given {
                port(port)
                body(
                    """
                    {
                        "title": "Test movie",
                        "releaseDate": "2020-06-25"
                    }
                """.trimIndent()
                )
                header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            } When {
                post(MoviesController.MOVIES_PATH)
            } Then {
                statusCode(201)
                header("location", startsWith(MoviesController.MOVIES_PATH))
            }
        }

        "should return bad request status when movie data is not complete" {
            Given {
                port(port)
                body(
                    """
                    { "title": "Test" }
                """.trimIndent()
                )
                header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            } When {
                post(MoviesController.MOVIES_PATH)
            } Then {
                statusCode(400)
            }
        }

        "should return conflict status with location when movie already exists" {
            val body = """
                    {
                        "title": "Test movie 2",
                        "releaseDate": "2020-06-25"
                    }
                """.trimIndent()
            val location = addMovie(port, body)
            Given {
                port(port)
                body(body)
                header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            } When {
                post(MoviesController.MOVIES_PATH)
            } Then {
                statusCode(409)
                header(HttpHeaders.LOCATION, location)
            }
        }
    }

    override fun listeners(): List<TestListener> = listOf(SpringListener)
}