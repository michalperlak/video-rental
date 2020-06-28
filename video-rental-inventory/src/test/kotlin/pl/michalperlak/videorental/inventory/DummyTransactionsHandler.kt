package pl.michalperlak.videorental.inventory

import pl.michalperlak.videorental.common.transactions.TransactionIsolation
import pl.michalperlak.videorental.common.transactions.TransactionsHandler

object DummyTransactionsHandler : TransactionsHandler {
    override fun <T> inTransaction(repetitionAttempts: Int, isolation: TransactionIsolation, callback: () -> T): T =
        callback()
}