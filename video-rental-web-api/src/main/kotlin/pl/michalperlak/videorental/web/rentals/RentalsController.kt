package pl.michalperlak.videorental.web.rentals

import org.springframework.web.bind.annotation.RestController
import pl.michalperlak.videorental.rentals.Rentals

@RestController
class RentalsController(
    private val rentals: Rentals
) {
}