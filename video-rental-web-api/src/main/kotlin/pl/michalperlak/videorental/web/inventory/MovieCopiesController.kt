package pl.michalperlak.videorental.web.inventory

import arrow.core.getOrElse
import arrow.core.getOrHandle
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.michalperlak.videorental.inventory.Inventory
import pl.michalperlak.videorental.inventory.dto.NewMovieCopy
import pl.michalperlak.videorental.inventory.error.ErrorAddingMovieCopy
import pl.michalperlak.videorental.inventory.error.InvalidMovieId
import pl.michalperlak.videorental.inventory.error.MovieCopyAddingError
import pl.michalperlak.videorental.inventory.error.MovieIdNotFound
import pl.michalperlak.videorental.web.inventory.MovieCopiesController.Companion.MOVIE_COPIES_PATH
import java.net.URI

@RestController
@RequestMapping(MOVIE_COPIES_PATH)
class MovieCopiesController(
    private val inventory: Inventory
) {
    @PostMapping
    fun addNewCopy(@RequestBody movieCopy: NewMovieCopy): ResponseEntity<*> = inventory
        .addNewCopy(movieCopy)
        .map { created(it.copyId) }
        .getOrHandle { errorAddingCopy(it) }

    @GetMapping("/{copyId}")
    fun getCopy(@PathVariable copyId: String): ResponseEntity<*> = inventory
        .getCopy(copyId)
        .map { ResponseEntity.ok(it) }
        .getOrElse {
            ResponseEntity
                .notFound()
                .build()
        }

    private fun errorAddingCopy(error: MovieCopyAddingError): ResponseEntity<Any> =
        when (error) {
            is InvalidMovieId,
            is MovieIdNotFound ->
                ResponseEntity.notFound().build()
            is ErrorAddingMovieCopy ->
                ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build()
        }

    private fun created(id: String): ResponseEntity<Any> =
        ResponseEntity
            .created(URI.create("${MOVIE_COPIES_PATH}/$id"))
            .build()

    companion object {
        const val MOVIE_COPIES_PATH = "/api/copies"
    }
}