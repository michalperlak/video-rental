package pl.michalperlak.videorental.common.transactions.exposed

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import pl.michalperlak.videorental.common.transactions.TransactionIsolation
import pl.michalperlak.videorental.common.transactions.TransactionsHandler

class ExposedTransactionsHandler(
    private val database: Database
) : TransactionsHandler {
    override fun <T> inTransaction(repetitionAttempts: Int, isolation: TransactionIsolation, callback: () -> T): T =
        transaction(
            transactionIsolation = isolation.value,
            repetitionAttempts = repetitionAttempts,
            db = database
        ) { callback() }
}