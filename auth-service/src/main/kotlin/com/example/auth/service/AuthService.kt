package com.example.auth.service

import com.example.auth.model.Role
import com.example.auth.model.User
import com.example.auth.model.dto.AuthResponse
import com.example.auth.model.dto.LoginRequest
import com.example.auth.model.dto.RegisterRequest
import com.example.auth.model.dto.UserDto
import com.example.auth.repository.RoleRepository
import com.example.auth.repository.UserRepository
import com.example.auth.service.imp.UserDetailsImpl
import com.example.auth.utils.JwtUtils
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtils: JwtUtils,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder
) {
    companion object {
        private val log = LoggerFactory.getLogger(AuthService::class.java)
    }

    @Transactional
    fun authenticateUser(loginRequest: LoginRequest): AuthResponse {
        try {
            val authentication: Authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    loginRequest.username,
                    loginRequest.password
                )
            )

            SecurityContextHolder.getContext().authentication = authentication
            val userDetails = authentication.principal as UserDetailsImpl
            val userDto = UserDto.fromUser(userDetails)
            val accessToken = jwtUtils.generateJwtToken(userDetails)

            return AuthResponse(
                accessToken = accessToken,
                user = userDto
            )
        } catch (e: BadCredentialsException) {
            throw e
        } catch (e: Exception) {
            throw e
        }
    }

    @Transactional
    fun registerUser(registerRequest: RegisterRequest): AuthResponse {
        if (userRepository.existsByUsername(registerRequest.username)) {
            throw IllegalArgumentException("Username is already taken!")
        }

        if (userRepository.existsByEmail(registerRequest.email)) {
            throw IllegalArgumentException("Email is already in use!")
        }

        val user = User(
            username = registerRequest.username,
            email = registerRequest.email,
            password = passwordEncoder.encode(registerRequest.password),
            isActive = true
        )

        val userRole = roleRepository.findByName(Role.ROLE_STUDENT)
            ?: throw IllegalStateException("User Role not found")

        user.addRole(userRole)
        val savedUser = userRepository.save(user)

        val userDetails = UserDetailsImpl.build(savedUser)
        val jwt = jwtUtils.generateJwtToken(userDetails)

        return AuthResponse(
            accessToken = jwt,
            user = UserDto.fromUser(userDetails)
        )
    }
}