package pl.michalperlak.videorental.rentals

import pl.michalperlak.videorental.rentals.domain.Return
import pl.michalperlak.videorental.rentals.domain.ReturnsRepository

internal class FailingReturnsRepository(
    private val errorProducer: () -> Throwable
) : ReturnsRepository {

    override fun addReturn(copiesReturn: Return): Return {
        throw errorProducer()
    }
}