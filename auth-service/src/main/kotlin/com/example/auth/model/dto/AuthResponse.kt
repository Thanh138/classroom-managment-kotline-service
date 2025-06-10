package com.example.auth.model.dto

data class AuthResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val user: UserDto
)