package pl.michalperlak.videorental.inventory.domain

interface MovieCopiesRepository {
    fun addCopy(movieCopy: MovieCopy): MovieCopy
}