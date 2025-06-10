package com.example.auth.model.dto

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)