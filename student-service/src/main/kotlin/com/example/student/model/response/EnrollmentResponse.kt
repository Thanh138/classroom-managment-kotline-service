package com.example.student.model.response

import java.time.Instant

data class EnrollmentResponse(
    val id: Long,
    val studentId: Long,
    val classId: Long,
    val enrolledById: Long,
    val status: String,
    val enrolledAt: Instant
)
