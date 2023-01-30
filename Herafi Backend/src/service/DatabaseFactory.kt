package com.nooblabs.service

import com.nooblabs.models.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop
import org.jetbrains.exposed.sql.transactions.transaction

interface IDatabaseFactory {
    fun init()

    suspend fun <T> dbQuery(block: () -> T): T

    suspend fun drop()
}

class DatabaseFactory : IDatabaseFactory {

    override fun init() {
        Database.connect(hikari())
        transaction {
            create(Users, Followings, Projects, Tags, ProjectTags, FavoriteProject, Comments, ProjectComment)

            //NOTE: Insert initial rows if any here
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = "org.h2.Driver"
//            jdbcUrl = "jdbc:h2:tcp://localhost/~/realworldtest"
            jdbcUrl = "jdbc:h2:tcp://localhost/~/test"
            username = "sa"
            password = ""
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }
        return HikariDataSource(config)
    }

    override suspend fun <T> dbQuery(block: () -> T): T = withContext(Dispatchers.IO) {
        transaction { block() }
    }

    override suspend fun drop() {
        dbQuery { drop(Users, Followings, Projects, Tags, ProjectTags, FavoriteProject, Comments, ProjectComment) }
    }
}