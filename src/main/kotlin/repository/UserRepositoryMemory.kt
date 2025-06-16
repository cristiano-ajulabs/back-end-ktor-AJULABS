package com.example.repository

import com.example.model.User

object UserRepositoryMemory {
    private val users = mutableListOf<User>()

    fun addUser(user: User): User {
        users.add(user)
        return user
    }

    fun findByEmail(email:String): User? {
        return users.find { it.email == email }
    }

}