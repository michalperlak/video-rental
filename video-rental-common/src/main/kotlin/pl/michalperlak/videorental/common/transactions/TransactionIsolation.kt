package pl.michalperlak.videorental.common.transactions

import java.sql.Connection

enum class TransactionIsolation(
    val value: Int
) {
    READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),
    READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),
    REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
    SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

    companion object {
        val DEFAULT_ISOLATION_LEVEL: TransactionIsolation = READ_UNCOMMITTED
    }
}