package pl.michalperlak.videorental.rentals.infr

import pl.michalperlak.videorental.rentals.domain.Return
import pl.michalperlak.videorental.rentals.domain.ReturnId
import pl.michalperlak.videorental.rentals.domain.ReturnsRepository
import java.util.concurrent.ConcurrentHashMap

internal class InMemoryReturnsRepository : ReturnsRepository {
    private val returns: MutableMap<ReturnId, Return> = ConcurrentHashMap()

    override fun addReturn(copiesReturn: Return): Return = copiesReturn.apply { returns[id] = this }

    fun getAll(): List<Return> = returns.values.toList()
}