package pl.michalperlak.videorental.web.inventory

import arrow.core.getOrHandle
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.michalperlak.videorental.inventory.Inventory
import pl.michalperlak.videorental.inventory.dto.NewMovie
import pl.michalperlak.videorental.inventory.error.ErrorAddingMovie
import pl.michalperlak.videorental.inventory.error.MovieAddingError
import pl.michalperlak.videorental.inventory.error.MovieAlreadyExists
import pl.michalperlak.videorental.web.inventory.MoviesController.Companion.MOVIES_PATH
import java.net.URI

@RestController
@RequestMapping(MOVIES_PATH)
class MoviesController(
    private val inventory: Inventory
) {

    @PostMapping
    fun addMovie(@RequestBody newMovie: NewMovie): ResponseEntity<*> = inventory
        .addMovie(newMovie)
        .map { created(it.id) }
        .getOrHandle { errorAddingMovie(it) }

    private fun errorAddingMovie(error: MovieAddingError): ResponseEntity<Any> =
        when (error) {
            is MovieAlreadyExists ->
                ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .header(HttpHeaders.LOCATION, "$MOVIES_PATH/${error.movieId}")
                    .build()
            is ErrorAddingMovie ->
                ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build()
        }

    private fun created(id: String): ResponseEntity<*> =
        ResponseEntity
            .created(URI.create("$MOVIES_PATH/$id"))
            .build<Any>()

    companion object {
        const val MOVIES_PATH = "/api/movies"
    }
}