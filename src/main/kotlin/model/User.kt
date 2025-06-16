package com.example.model

import java.util.UUID
import kotlinx.serialization.Serializable


data class User(
    val id : UUID,
    val name: String,
    val email: String,
    val passwordHash: String
)

@Serializable
data class UserResponse(
    val id : String,
    val name : String,
    val email : String,
)

fun User.toResponse() = UserResponse(
    id = this.id.toString(),
    name = this.name,
    email = this.email
)