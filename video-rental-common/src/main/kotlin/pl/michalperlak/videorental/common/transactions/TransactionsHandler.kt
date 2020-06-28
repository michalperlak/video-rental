package pl.michalperlak.videorental.common.transactions

interface TransactionsHandler {
    fun <T> inTransaction(
        repetitionAttempts: Int = 1,
        isolation: TransactionIsolation = TransactionIsolation.DEFAULT_ISOLATION_LEVEL,
        callback: () -> T): T
}