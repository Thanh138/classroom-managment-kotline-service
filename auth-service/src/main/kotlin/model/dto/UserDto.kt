package com.example.model.dto

data class UserDto(
    val id: Long,
    val username: String,
    val email: String,
    val roles: List<String>
)