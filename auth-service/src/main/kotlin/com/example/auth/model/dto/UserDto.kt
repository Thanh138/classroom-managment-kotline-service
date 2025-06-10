package com.example.auth.model.dto

import com.example.auth.model.User
import com.example.auth.service.imp.UserDetailsImpl

data class UserDto(
    val id: Long,
    val username: String,
    val email: String,
    val roles: Set<String> = emptySet()
) {
    companion object {
        fun fromUser(user: User) : UserDto {
            return UserDto(
                id = user.id,
                username = user.username,
                email = user.email,
                roles = user.roles.map { it.name }.toSet()
            )
        }

        fun fromUser(userDetails: UserDetailsImpl): UserDto {
            return UserDto(
                id = userDetails.id,
                username = userDetails.username,
                email = userDetails.email,
                roles = userDetails.authorities.map { it.authority }.toSet()
            )
        }
    }
}