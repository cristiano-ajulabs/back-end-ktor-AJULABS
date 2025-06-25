package com.example.repository

import com.example.model.User
import com.example.model.UserTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object UserRepositoryDb {

    fun add(user: User) {
        transaction {
            UserTable.insert {
                it[id] = user.id
                it[name] = user.name
                it[email] = user.email
                it[passwordHash] = user.passwordHash
            }
        }
    }

    fun findByEmail(email: String): User? = transaction {
        UserTable
            .selectAll()
            .map {
                User(
                    id = it[UserTable.id],
                    name = it[UserTable.name],
                    email = it[UserTable.email],
                    passwordHash = it[UserTable.passwordHash]
                )
            }
            .find { it.email == email }
    }

    fun getAll(): List<User> {
        return transaction {
            UserTable.selectAll().map {
                User(
                    id = it[UserTable.id],
                    name = it[UserTable.name],
                    email = it[UserTable.email],
                    passwordHash = it[UserTable.passwordHash]
                )
            }
        }
    }
    fun delete(id: UUID): Boolean {
        return transaction {
            val rowsDeleted = UserTable.deleteWhere { UserTable.id eq id }
            rowsDeleted > 0
        }
    }

    fun update(id: UUID, name: String, email: String): Boolean {
        return transaction {
            val updatedRows = UserTable.update({ UserTable.id eq id }) {
                it[UserTable.name] = name
                it[UserTable.email] = email
            }
            updatedRows > 0
        }
    }


}