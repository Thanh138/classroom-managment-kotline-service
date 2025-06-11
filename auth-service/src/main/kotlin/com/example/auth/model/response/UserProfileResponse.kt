package com.example.auth.model.response

data class UserProfileResponse(
    val id: Long,
    val username: String,
    val email: String,
    val roles: Set<String>,
    val isActive: Boolean
)