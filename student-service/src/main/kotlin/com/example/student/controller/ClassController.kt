package com.example.student.controller


import com.example.student.model.request.CreateClassRequest
import com.example.student.model.response.ClassResponse
import com.example.student.service.ClassService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/classes")
class ClassController(
    private val classService: ClassService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createClass(
        @Valid @RequestBody request: CreateClassRequest,
        @AuthenticationPrincipal jwt: Jwt
    ): ClassResponse {
        val username = jwt.subject
            ?: throw IllegalArgumentException("Invalid username in token")

        return classService.createClass(request, username)
    }

    @GetMapping("/{classId}")
    fun getClassById(@PathVariable classId: Long): ClassResponse {
        return classService.getClassById(classId)
    }

    @GetMapping("/teacher/{username}")
    fun getClassesByTeacher(@PathVariable username: String): List<ClassResponse> {
        return classService.getClassesByTeacher(username)
    }
}