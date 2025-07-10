package com.gub.database

import org.jetbrains.exposed.sql.Database

/**
 * Commands to set up the PostgreSQL database:
 *
 * 1. psql postgres
 * 2. CREATE USER ktoruser WITH PASSWORD 'ktorpass';
 * 3. ALTER USER ktoruser CREATEDB;
 * 4. CREATE DATABASE ktordb WITH OWNER ktoruser;
 * 5. \q
 * 
 * For Docker environment, the database is automatically configured.
 * */

object DatabaseFactory {

    fun init() {
        val databaseUrl = System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/ktordb"
        val databaseUser = System.getenv("DATABASE_USER") ?: "ktoruser"
        val databasePassword = System.getenv("DATABASE_PASSWORD") ?: "ktorpass"
        
        Database.connect(
            url = databaseUrl,
            driver = "org.postgresql.Driver",
            user = databaseUser,
            password = databasePassword
        )
        
        println("✅ Database connected: $databaseUrl")
    }
}