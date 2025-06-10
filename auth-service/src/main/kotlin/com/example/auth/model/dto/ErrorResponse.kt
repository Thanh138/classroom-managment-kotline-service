package com.example.auth.model.dto

data class ErrorResponse(
    val status: Int,
    val message: String,
    val path: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
