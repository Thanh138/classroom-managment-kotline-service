package com.example.student.service

import com.example.student.exception.AlreadyEnrolledException
import com.example.student.exception.ClassFullException
import com.example.student.exception.ResourceNotFoundException
import com.example.student.model.Class
import com.example.student.model.Enrollment
import com.example.student.model.request.EnrollStudentRequest
import com.example.student.model.response.EnrollmentResponse
import com.example.student.repository.ClassRepository
import com.example.student.repository.EnrollmentRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.*

@Service
class EnrollmentService(
    private val enrollmentRepository: EnrollmentRepository,
    private val classRepository: ClassRepository,
    private val userService: UserService,

    @Value("\${app.class.max-students:50}")
    private val maxStudentsPerClass: Int
) {
    @Transactional
    @PreAuthorize("hasAnyRole('TEACHER', 'STAFF', 'ADMIN')")
    fun enrollStudent(request: EnrollStudentRequest, enrolledById: Long): EnrollmentResponse {
        // Verify users and class exist
        userService.getUserById(request.studentId)
        val `class` = getClassById(request.classId)
        userService.getUserById(enrolledById)

        // Check if student is already enrolled
        if (enrollmentRepository.isStudentEnrolled(request.studentId, `class`.id)) {
            throw AlreadyEnrolledException("Student is already enrolled in this class")
        }

        // Check if class has available slots
        val currentEnrollments = enrollmentRepository.countByClassIdAndStatus(
            `class`.id,
            Enrollment.EnrollmentStatus.ACTIVE
        )

        if (currentEnrollments >= maxStudentsPerClass) {
            throw ClassFullException("Class has reached maximum capacity of $maxStudentsPerClass students")
        }

        val enrollment = Enrollment.create(
            studentId = request.studentId,
            classId = `class`.id,
            enrolledById = enrolledById,
            status = Enrollment.EnrollmentStatus.ACTIVE
        )

        val savedEnrollment = enrollmentRepository.save(enrollment)
        return savedEnrollment.toResponse()
    }

    @PreAuthorize("hasAnyRole('TEACHER', 'STAFF', 'ADMIN')")
    fun getClassEnrollments(classId: Long): List<EnrollmentResponse> {
        return enrollmentRepository.findActiveEnrollmentsByClassId(classId)
            .map { it.toResponse() }
    }

    private fun getClassById(classId: Long): Class {
        return classRepository.findById(classId)
            .orElseThrow { ResourceNotFoundException("Class not found with id: $classId") }
    }

    // Extension function to convert Enrollment to EnrollmentResponse
    private fun Enrollment.toResponse() = EnrollmentResponse(
        id = id,
        studentId = studentId,
        classId = classId,
        enrolledById = enrolledById,
        status = status.name,
        enrolledAt = enrolledAt
    )
}