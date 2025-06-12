package com.example.student.controller


import com.example.student.model.request.EnrollStudentRequest
import com.example.student.model.response.EnrollmentResponse
import com.example.student.service.EnrollmentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/enrollments")
class EnrollmentController(
    private val enrollmentService: EnrollmentService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun enrollStudent(
        @Valid @RequestBody request: EnrollStudentRequest,
        @AuthenticationPrincipal jwt: Jwt
    ): EnrollmentResponse {
        val enrolledById = jwt.subject?.toLong()
            ?: throw IllegalArgumentException("Invalid user ID in token")

        return enrollmentService.enrollStudent(request, enrolledById)
    }

    @GetMapping("/classes/{classId}/students")
    fun getClassEnrollments(@PathVariable classId: Long): List<EnrollmentResponse> {
        return enrollmentService.getClassEnrollments(classId)
    }
}