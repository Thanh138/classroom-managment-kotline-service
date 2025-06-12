package com.example.auth.model.response

import com.example.auth.model.dto.UserDto
import java.time.Instant

data class UserResponse(
    val id: Long,
    val username: String,
    val email: String,
    val roles: Set<String> = emptySet(),
    val isActive: Boolean = true,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
) {
    companion object {
        fun fromUserDto(dto: UserDto) = UserResponse(
            id = dto.id,
            username = dto.username,
            email = dto.email,
            roles = dto.roles,
            isActive = dto.isActive,
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt
        )
    }
}