package pl.michalperlak.videorental.web.inventory

import org.springframework.boot.autoconfigure.flyway.FlywayDataSource
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.michalperlak.videorental.inventory.Inventory
import pl.michalperlak.videorental.inventory.InventoryFactory
import java.time.Clock
import javax.sql.DataSource

@Configuration
class InventoryConfig {

    @Bean
    fun inventory(clock: Clock): Inventory = InventoryFactory.create(inventoryDataSource(), clock)

    @Bean
    @FlywayDataSource
    @ConfigurationProperties(prefix = "inventory.datasource")
    fun inventoryDataSource(): DataSource = DataSourceBuilder
        .create()
        .build()
}