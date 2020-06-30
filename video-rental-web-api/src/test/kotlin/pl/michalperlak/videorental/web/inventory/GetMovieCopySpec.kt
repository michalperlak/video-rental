package pl.michalperlak.videorental.web.inventory

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import pl.michalperlak.videorental.inventory.dto.MovieCopy
import pl.michalperlak.videorental.web.util.extractBody
import java.util.UUID

@SpringBootTest(webEnvironment = RANDOM_PORT)
class GetMovieCopySpec : StringSpec() {

    @LocalServerPort
    private var port: Int = 0

    init {
        "should return not found status when copy does not exist" {
            val movieCopyId = UUID.randomUUID().toString()
            Given {
                port(port)
            } When {
                get("${MovieCopiesController.MOVIE_COPIES_PATH}/$movieCopyId")
            } Then {
                statusCode(404)
            }
        }

        "should return copy data when it exists" {
            val movieId = addMovie(port, """{ "title": "Movie 123", "releaseDate": "2020-06-27" }""")
            val movieCopyId = addMovieCopy(movieId)
            Given {
                port(port)
            } When {
                get("${MovieCopiesController.MOVIE_COPIES_PATH}/$movieCopyId")
            } Then {
                statusCode(200)
                val responseData = extract()
                    .extractBody<MovieCopy>()
                responseData.copyId shouldBe movieCopyId
                responseData.movieId shouldBe movieId
            }
        }
    }

    private fun addMovieCopy(movieId: String): String =
        Given {
            port(port)
            body(""" { "movieId": "$movieId" }""")
            header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        } When {
            post(MovieCopiesController.MOVIE_COPIES_PATH)
        } Extract {
            header(HttpHeaders.LOCATION)
                .substringAfter("${MovieCopiesController.MOVIE_COPIES_PATH}/")
        }

    override fun listeners(): List<TestListener> = listOf(SpringListener)
}