package com.example.auth.model.request

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)