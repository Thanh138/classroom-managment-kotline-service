package com.example.auth.model.response

data class UserRolesResponse(
    val userId: Long,
    val roles: Set<String>
)