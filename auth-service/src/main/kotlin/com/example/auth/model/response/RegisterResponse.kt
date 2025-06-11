package com.example.auth.model.response

data class RegisterResponse(
    val message: String = "User registered successfully",
    val userId: Long
)