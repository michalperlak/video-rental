package pl.michalperlak.videorental.inventory.domain

import arrow.core.Option

internal interface MovieCopiesRepository {
    fun addCopy(movieCopy: MovieCopy): MovieCopy
    fun findById(copyId: MovieCopyId): Option<MovieCopy>
}