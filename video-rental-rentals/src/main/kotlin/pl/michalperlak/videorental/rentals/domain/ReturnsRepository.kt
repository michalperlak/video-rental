package pl.michalperlak.videorental.rentals.domain

internal interface ReturnsRepository {
    fun addReturn(copiesReturn: Return): Return
}