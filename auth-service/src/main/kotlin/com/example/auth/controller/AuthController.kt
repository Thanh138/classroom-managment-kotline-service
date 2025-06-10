package com.example.auth.controller

import com.example.auth.model.dto.AuthResponse
import com.example.auth.model.dto.LoginRequest
import com.example.auth.model.dto.RegisterRequest
import com.example.auth.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService : AuthService
) {
    @GetMapping
    fun test() = "Test endpoint is working!"

    @PostMapping("/login")
    fun authenticateUser(@Valid @RequestBody request: LoginRequest) : ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.authenticateUser(request))
    }

    @PostMapping("/register")
    fun registerUser(@Valid @RequestBody request: RegisterRequest) : ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.registerUser(request))
    }
}