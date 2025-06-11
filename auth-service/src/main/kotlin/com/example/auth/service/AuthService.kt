package com.example.auth.service

import com.example.auth.model.Role
import com.example.auth.model.User
import com.example.auth.model.response.AuthResponse
import com.example.auth.model.request.LoginRequest
import com.example.auth.model.request.RegisterRequest
import com.example.auth.model.response.RegisterResponse
import com.example.auth.model.dto.UserDto
import com.example.auth.model.response.UserProfileResponse
import com.example.auth.repository.RoleRepository
import com.example.auth.repository.UserRepository
import com.example.auth.service.imp.UserDetailsImpl
import com.example.auth.utils.JwtUtils
import org.springframework.transaction.annotation.Transactional
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
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
            log.info("Attempting to authenticate user: ${loginRequest.username}")
            
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    loginRequest.username,
                    loginRequest.password
                )
            )

            log.info("Authentication successful for user: ${loginRequest.username}")
            
            SecurityContextHolder.getContext().authentication = authentication
            val userDetails = authentication.principal as UserDetailsImpl
            val user = userRepository.findByUsername(loginRequest.username)
                ?: throw UsernameNotFoundException("User not found after authentication")
                
            val userDto = UserDto.fromUser(userDetails)
            val accessToken = jwtUtils.generateJwtToken(userDetails)

            log.info("Generated JWT token for user: ${user.username}")
            
            return AuthResponse(
                accessToken = accessToken,
                user = userDto
            )
        } catch (e: BadCredentialsException) {
            log.error("Authentication failed for user ${loginRequest.username}: ${e.message}")
            throw BadCredentialsException("Invalid username or password")
        } catch (e: UsernameNotFoundException) {
            log.error("User not found: ${loginRequest.username}")
            throw e
        } catch (e: Exception) {
            log.error("Unexpected error during authentication: ${e.message}", e)
            throw RuntimeException("Authentication failed: ${e.message}")
        }
    }

    @Transactional
    fun registerUser(registerRequest: RegisterRequest): RegisterResponse {
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

        return RegisterResponse(userId = savedUser.id)
    }

    @Transactional(readOnly  = true)
    fun getCurrentUserProfile(): UserProfileResponse {
        val authentication : Authentication = SecurityContextHolder.getContext().authentication ?: throw IllegalStateException("Authentication is null")
        val userDetails = authentication.principal as? UserDetailsImpl
            ?: throw IllegalStateException("User not authenticated")

        val user = userRepository.findByUsername(userDetails.username)
            ?: throw UsernameNotFoundException("User not found with username: ${userDetails.username}")

        return UserProfileResponse(
            id = user.id,
            username = user.username,
            email = user.email,
            roles = user.roles.map { it.name }.toSet(),
            isActive = user.isActive
        )
    }
}