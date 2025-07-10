package com.gub.data.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseConfig {

    fun init() {
        val config = HikariConfig().apply {
            driverClassName = "org.h2.Driver"
            jdbcUrl = "jdbc:h2:mem:dashboard;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
            username = "sa"
            password = ""
            maximumPoolSize = 10
        }

        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)

        createTables()
    }

    private fun createTables() {
        transaction {
            SchemaUtils.create(AiControlMetrics, TrafficMetrics, SystemMetrics)
        }
    }
}

// For production PostgreSQL
object ProductionDatabaseConfig {
    fun init() {
        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/dashboard_db"
            username = System.getenv("DB_USER") ?: "postgres"
            password = System.getenv("DB_PASSWORD") ?: "password"
            maximumPoolSize = 20
        }

        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)

        createTables()
    }

    private fun createTables() {
        transaction {
            SchemaUtils.create(AiControlMetrics, TrafficMetrics, SystemMetrics)
        }
    }
}