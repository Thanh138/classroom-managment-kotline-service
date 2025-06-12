package com.example.auth.service

import com.example.auth.model.User
import com.example.auth.repository.RoleRepository
import com.example.auth.repository.UserRepository
import com.example.auth.utils.JwtUtils
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class RoleService(
    private val roleRepository: RoleRepository,
    private val userRepository: UserRepository,
    private val jwtUtils: JwtUtils
) {

    @Transactional
    fun addRoleToUser(userId: Long, roleName: String) : User {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found with id: $userId") }

        val role = roleRepository.findByName(roleName)
            ?: throw IllegalArgumentException("Role not found with name: $roleName")
        if(!user.roles.any {it.name == roleName}){
            user.addRole(role)
            userRepository.save(user)
        }
        return user
    }

    @Transactional
    fun removeRoleFromUser(userId: Long, roleName: String): User {
        val user = userRepository.findById(userId)
            .orElseThrow { EntityNotFoundException("User not found with id: $userId") }

        val role = user.roles.find { it.name == roleName }
            ?: return user

        user.roles.remove(role)
        return userRepository.save(user)
    }
}