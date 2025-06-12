package com.example.student.model.dto

import java.time.Instant

data class EnrollmentDto(
    val id: Long,
    val studentId: Long,
    val classId: Long,
    val enrolledById: Long,
    val status: String,
    val enrolledAt: Instant,
    val completedAt: Instant?
)