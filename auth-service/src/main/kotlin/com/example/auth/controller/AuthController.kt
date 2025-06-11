package com.example.auth.controller

import com.example.auth.model.response.AuthResponse
import com.example.auth.model.request.LoginRequest
import com.example.auth.model.request.RegisterRequest
import com.example.auth.model.response.RegisterResponse
import com.example.auth.model.response.UserProfileResponse
import com.example.auth.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService : AuthService
) {

    @GetMapping("/me")
    @Operation(
        summary = "Get current user profile",
        description = "Get the profile of the currently authenticated user",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun getCurrentUserProfile(): ResponseEntity<UserProfileResponse> {
        return ResponseEntity.ok(authService.getCurrentUserProfile())
    }
    @PostMapping("/login")
    fun authenticateUser(@Valid @RequestBody request: LoginRequest) : ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.authenticateUser(request))
    }

    @PostMapping("/register")
    fun registerUser(@Valid @RequestBody request: RegisterRequest) : ResponseEntity<RegisterResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(request))
    }
}