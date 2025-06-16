package com.example.model
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val name : String,
    val email : String,
    val password : String,
)