package pl.michalperlak.videorental.web.rentals

import arrow.core.getOrHandle
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.michalperlak.videorental.rentals.Rentals
import pl.michalperlak.videorental.rentals.error.MovieNotAvailable
import pl.michalperlak.videorental.web.rentals.RentalsController.Companion.RENTALS_PATH

@RestController
@RequestMapping(RENTALS_PATH)
class RentalsController(
    private val rentals: Rentals
) {
    @PostMapping
    fun createRental(@RequestBody rental: RentalDto): ResponseEntity<*> =
        rentals
            .newRental(rental.convert())
            .map {
                ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.LOCATION, "${RENTALS_PATH}/${it.rentalId}")
                    .body(it)
            }
            .getOrHandle {
                when (it) {
                    is MovieNotAvailable -> ResponseEntity.badRequest().build()
                    else -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                }
            }

    companion object {
        const val RENTALS_PATH = "/api/rentals"
    }
}