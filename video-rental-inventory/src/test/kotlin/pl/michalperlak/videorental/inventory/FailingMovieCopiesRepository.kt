package pl.michalperlak.videorental.inventory

import pl.michalperlak.videorental.inventory.domain.MovieCopiesRepository
import pl.michalperlak.videorental.inventory.domain.MovieCopy

class FailingMovieCopiesRepository(
    private val errorProducer: () -> Throwable
) : MovieCopiesRepository {

    override fun addCopy(movieCopy: MovieCopy): MovieCopy {
        throw errorProducer()
    }
}