package pl.michalperlak.videorental.web.inventory

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.web.server.LocalServerPort
import pl.michalperlak.videorental.inventory.dto.Movie
import java.time.LocalDate

@SpringBootTest(webEnvironment = RANDOM_PORT)
class GetMovieSpec : StringSpec() {

    @LocalServerPort
    private var port: Int = 0

    init {
        "should return not found status when movie does not exist" {
            val movieId = "abcd-efgh-1234"
            Given {
                port(port)
            } When {
                get("${MoviesController.MOVIES_PATH}/$movieId")
            } Then {
                statusCode(404)
            }
        }

        "should return movie data when movie exists" {
            val title = "test movie 123"
            val releaseDate = "2020-06-25"
            val movieId = registerMovie(title, releaseDate)
            Given {
                port(port)
            } When {
                get("${MoviesController.MOVIES_PATH}/$movieId")
            } Then {
                statusCode(200)
                val responseData = extract()
                    .extractBody<Movie>()
                responseData.releaseDate shouldBe LocalDate.parse(releaseDate)
                responseData.title shouldBe title
            }
        }
    }

    private fun registerMovie(title: String, releaseDate: String): String {
        val body = """
            { "title": "$title", "releaseDate": "$releaseDate" }
        """.trimIndent()
        return addMovie(port, body)
            .substringAfter(MoviesController.MOVIES_PATH + "/")
    }

    override fun listeners(): List<TestListener> = listOf(SpringListener)
}