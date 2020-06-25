package pl.michalperlak.videorental.inventory.domain

internal interface MovieCopiesRepository {
    fun addCopy(movieCopy: MovieCopy): MovieCopy
}