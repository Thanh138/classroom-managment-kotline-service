package com.example.auth.controller

import com.example.auth.model.RoleAction
import com.example.auth.model.request.UpdateRolesRequest
import com.example.auth.model.response.UserRolesResponse
import com.example.auth.repository.UserRepository
import com.example.auth.service.RoleService
import jakarta.persistence.EntityNotFoundException
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/roles")
class RoleController(
    private val roleService: RoleService,
    private val userRepository: UserRepository
) {
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{userId}")
    fun updateUserRole(
        @PathVariable userId: Long,
        @Valid @RequestBody request: UpdateRolesRequest
    ): ResponseEntity<UserRolesResponse> {
        val user = when (request.action) {
            RoleAction.ADD -> roleService.addRoleToUser(userId, request.roleName)
            RoleAction.REMOVE -> roleService.removeRoleFromUser(userId, request.roleName)
        }

        return ResponseEntity.ok(UserRolesResponse(
            userId = user.id,
            roles = user.roles.map { it.name }.toSet()
        ))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{userId}")
    fun getUserRoles(@PathVariable userId: Long): ResponseEntity<UserRolesResponse> {
        val user = userRepository.findById(userId)
            .orElseThrow { EntityNotFoundException("User not found with id: $userId") }

        return ResponseEntity.ok(UserRolesResponse(
            userId = user.id,
            roles = user.roles.map { it.name }.toSet()
        ))
    }
}