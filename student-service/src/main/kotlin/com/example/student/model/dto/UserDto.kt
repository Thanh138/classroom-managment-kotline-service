package com.example.student.model.dto

data class UserDto (
    val id: String,
    val username: String,
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val roles: List<String>
)