package pl.michalperlak.videorental.inventory.domain

interface MoviesRepository {
    fun addMovie(movie: Movie): Movie
}