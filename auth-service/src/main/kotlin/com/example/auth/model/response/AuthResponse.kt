package com.example.auth.model.response

import com.example.auth.model.dto.UserDto

data class AuthResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val user: UserDto
)