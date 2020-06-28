package pl.michalperlak.videorental.inventory.application

import arrow.core.ListK
import arrow.core.k
import pl.michalperlak.videorental.common.transactions.TransactionIsolation
import pl.michalperlak.videorental.common.transactions.TransactionsHandler
import pl.michalperlak.videorental.inventory.domain.MovieCopiesRepository
import pl.michalperlak.videorental.inventory.domain.MovieCopy
import pl.michalperlak.videorental.inventory.domain.MovieCopyStatus
import pl.michalperlak.videorental.inventory.domain.MovieId
import pl.michalperlak.videorental.inventory.domain.Rental
import pl.michalperlak.videorental.inventory.domain.RentalItem
import pl.michalperlak.videorental.inventory.error.CopiesNotAvailableException

internal class DefaultMovieRentalService(
    private val moviesCopiesRepository: MovieCopiesRepository,
    private val transactionsHandler: TransactionsHandler
) : MovieRentalService {

    override fun rent(rentalItems: ListK<RentalItem>): Rental =
        transactionsHandler.inTransaction(isolation = TransactionIsolation.REPEATABLE_READ) {
            rentalItems
                .flatMap(this::getCopies)
                .map { moviesCopiesRepository.updateCopy(it.copy(status = MovieCopyStatus.RENTED)) }
                .let { Rental(it) }
        }

    private fun getCopies(rentalItem: RentalItem): ListK<MovieCopy> =
        moviesCopiesRepository
            .findByMovieAndStatus(rentalItem.movieId, MovieCopyStatus.AVAILABLE)
            .take(rentalItem.copies)
            .toList()
            .k()
            .ensureCopiesAvailable(rentalItem.movieId, rentalItem.copies)

    private fun ListK<MovieCopy>.ensureCopiesAvailable(movieId: MovieId, copies: Int): ListK<MovieCopy> =
        if (size < copies) {
            throw CopiesNotAvailableException(movieId.toString(), copies)
        } else {
            this
        }
}