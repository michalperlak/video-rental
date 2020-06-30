package pl.michalperlak.videorental.web.rentals

import arrow.core.getOrHandle
import arrow.core.k
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.michalperlak.videorental.rentals.Rentals
import pl.michalperlak.videorental.rentals.error.CopiesNotRecognized
import pl.michalperlak.videorental.rentals.error.RentalNotRecognized
import pl.michalperlak.videorental.web.rentals.ReturnsController.Companion.RETURNS_PATH

@RestController
@RequestMapping(RETURNS_PATH)
class ReturnsController(
    private val rentals: Rentals
) {
    @PostMapping
    fun returnCopies(@RequestBody returnDto: ReturnDto): ResponseEntity<*> =
        rentals
            .returnCopies(rentalId = returnDto.rentalId, movieCopyIds = returnDto.copies.k())
            .map {
                ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.LOCATION, "$RETURNS_PATH/${it.returnId}")
                    .body(it)
            }
            .getOrHandle {
                when (it) {
                    is RentalNotRecognized,
                    is CopiesNotRecognized -> ResponseEntity.notFound().build()
                    else -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                }
            }

    companion object {
        const val RETURNS_PATH = "/api/returns"
    }
}