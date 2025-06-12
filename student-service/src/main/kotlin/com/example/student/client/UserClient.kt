package com.example.student.client

import com.example.common.dto.UserDto
import com.example.student.config.FeignClientConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "auth-service",
    url = "\${services.auth-service.url}",
    path = "/api/v1/users",
    configuration = [FeignClientConfig::class]
)
interface UserClient {
    @GetMapping("/{id}")
    fun getUserById(@PathVariable("id") id: Long): UserDto?

    @GetMapping("/{userId}/has-role/{role}")
    fun userHasRole(
        @PathVariable userId: Long,
        @PathVariable role: String
    ): Boolean

    @GetMapping("/exists/{username}")
    fun userExists(@PathVariable username: String): Boolean

    @GetMapping("/search")
    fun searchUsers(
        @RequestParam query: String,
        @RequestParam role: String? = null
    ): List<UserDto>

    @GetMapping("/username/{username}")
    fun getUserByUsername(@PathVariable username: String): UserDto?
}