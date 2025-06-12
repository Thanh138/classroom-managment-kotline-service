package com.example.auth.controller

import com.example.auth.model.dto.UserDto
import com.example.auth.model.response.UserResponse
import com.example.auth.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(private val userService: UserService) {

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'TEACHER')")
    fun getUserById(@PathVariable userId: Long): ResponseEntity<UserDto> {
        val user = userService.getUserById(userId)
        return ResponseEntity.ok(UserDto.fromUser(user))
    }

    @GetMapping("/{userId}/has-role/{role}")
    @PreAuthorize("isAuthenticated()")
    fun userHasRole(
        @PathVariable userId: Long,
        @PathVariable role: String
    ): ResponseEntity<Boolean> {
        val hasRole = userService.userHasRole(userId, role)
        return ResponseEntity.ok(hasRole)
    }

    @GetMapping("/exists/{username}")
    @PreAuthorize("isAuthenticated()")
    fun checkUsernameExists(@PathVariable username: String): ResponseEntity<Map<String, Boolean>> {
        val exists = userService.existsByUsername(username)
        return ResponseEntity.ok(mapOf("exists" to exists))
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("isAuthenticated()")
    fun getUserByUsername(@PathVariable username: String): ResponseEntity<UserResponse> {
        val userDto = userService.findByUsername(username)
        return ResponseEntity.ok(UserResponse.fromUserDto(userDto))
    }
}