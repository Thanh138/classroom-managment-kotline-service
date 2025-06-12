package com.example.auth.service

import com.example.auth.exception.ResourceNotFoundException
import com.example.auth.model.User
import com.example.auth.model.dto.UserDto
import com.example.auth.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional(readOnly = true)
    fun getUserById(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User not found with id: $userId") }
    }

    @Transactional(readOnly = true)
    fun userHasRole(userId: Long, role: String): Boolean {
        return userRepository.existsByIdAndRolesName(userId, "ROLE_$role")
    }

    @Transactional(readOnly = true)
    fun existsByUsername(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }

    @Transactional(readOnly = true)
    fun findByUsername(username: String): UserDto {
        val user = userRepository.findByUsername(username)
            ?: throw ResourceNotFoundException("User not found with username: $username")
        return UserDto.fromUser(user)
    }

}