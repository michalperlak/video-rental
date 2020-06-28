package pl.michalperlak.videorental.inventory

import arrow.core.Option
import arrow.core.SequenceK
import pl.michalperlak.videorental.inventory.domain.MovieCopiesRepository
import pl.michalperlak.videorental.inventory.domain.MovieCopy
import pl.michalperlak.videorental.inventory.domain.MovieCopyId
import pl.michalperlak.videorental.inventory.domain.MovieCopyStatus
import pl.michalperlak.videorental.inventory.domain.MovieId

internal class FailingMovieCopiesRepository(
    private val errorProducer: () -> Throwable
) : MovieCopiesRepository {

    override fun addCopy(movieCopy: MovieCopy): MovieCopy {
        throw errorProducer()
    }

    override fun findById(copyId: MovieCopyId): Option<MovieCopy> {
        throw errorProducer()
    }

    override fun findByMovieAndStatus(movieId: MovieId, status: MovieCopyStatus): SequenceK<MovieCopy> {
        throw errorProducer()
    }
}