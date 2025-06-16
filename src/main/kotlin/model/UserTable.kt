package com.example.model

import org.jetbrains.exposed.sql.Table

object UserTable : Table() {
    val id = uuid("id")
    val name = varchar("name", 50)
    val email = varchar("email", 100).uniqueIndex()
    val passwordHash = varchar("passwordHash", 255)

    override val primaryKey = PrimaryKey(id)
}