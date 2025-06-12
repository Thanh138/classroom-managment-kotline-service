package com.example.auth.model.response

import com.example.auth.model.dto.UserDto

data class UserSearchResponse(
    val id: Long,
    val username: String,
    val email: String,
    val roles: Set<String> = emptySet(),
    val isActive: Boolean = true
) {
    companion object {
        fun fromUserDto(dto: UserDto) = UserSearchResponse(
            id = dto.id,
            username = dto.username,
            email = dto.email,
            roles = dto.roles,
            isActive = dto.isActive
        )
    }
}