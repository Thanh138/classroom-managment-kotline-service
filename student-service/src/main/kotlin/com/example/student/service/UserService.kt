package com.example.student.service


import com.example.common.dto.UserDto
import com.example.student.client.UserClient
import com.example.student.exception.ResourceNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userClient: UserClient
) {
    fun getUserById(userId: Long): UserDto {
        return userClient.getUserById(userId)
            ?: throw ResourceNotFoundException("User not found with id: $userId")
    }
}