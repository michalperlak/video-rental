package pl.michalperlak.videorental.inventory

import arrow.core.Option
import pl.michalperlak.videorental.inventory.domain.MovieCopiesRepository
import pl.michalperlak.videorental.inventory.domain.MovieCopy
import pl.michalperlak.videorental.inventory.domain.MovieCopyId

internal class FailingMovieCopiesRepository(
    private val errorProducer: () -> Throwable
) : MovieCopiesRepository {

    override fun addCopy(movieCopy: MovieCopy): MovieCopy {
        throw errorProducer()
    }

    override fun findById(copyId: MovieCopyId): Option<MovieCopy> {
        throw errorProducer()
    }
}