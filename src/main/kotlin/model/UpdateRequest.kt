package model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateRequest(
    val name: String,
    val email: String,
)