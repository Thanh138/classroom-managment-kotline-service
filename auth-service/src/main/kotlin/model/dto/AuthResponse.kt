package com.example.model.dto

data class AuthResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val user: UserDto
)