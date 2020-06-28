package pl.michalperlak.videorental.inventory.db

import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import org.flywaydb.core.Flyway
import org.h2.jdbcx.JdbcDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource

abstract class InMemoryDatabaseSpec(
    private val tables: List<Table> = emptyList(),
    dbName: String,
    body: InMemoryDatabaseSpec.() -> Unit = {}
) : StringSpec() {

    private val datasource: DataSource = JdbcDataSource().apply {
        user = DB_USER
        setURL(dbUrl(dbName))
    }
    val database = Database.connect(datasource)

    init {
        this.body()
    }

    override fun beforeSpec(spec: Spec) {
        val flyway = Flyway
            .configure()
            .dataSource(datasource)
            .load()
        flyway.migrate()
    }

    override fun afterTest(testCase: TestCase, result: TestResult) {
        transaction(database) {
            tables.forEach { it.deleteAll() }
        }
    }

    companion object {
        private const val DB_USER = "sa"

        private fun dbUrl(name: String): String = "jdbc:h2:mem:$name;DB_CLOSE_DELAY=-1"
    }
}