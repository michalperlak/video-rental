package pl.michalperlak.videorental.inventory.domain

import arrow.core.Option
import arrow.core.SequenceK

internal interface MovieCopiesRepository {
    fun addCopy(movieCopy: MovieCopy): MovieCopy
    fun findById(copyId: MovieCopyId): Option<MovieCopy>
    fun findByMovieAndStatus(movieId: MovieId, status: MovieCopyStatus): SequenceK<MovieCopy>
}